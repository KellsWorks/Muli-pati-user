package app.mulipati.ui.trips.upcoming

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.mulipati.data.chat.Upcoming
import app.mulipati.databinding.FragmentUpcomingBinding
import app.mulipati.epoxy.upcoming.UpcomingEpoxyController
import app.mulipati.ui.dashboard.TripsViewModel
import app.mulipati.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private lateinit var upcomingBinding: FragmentUpcomingBinding

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var upcomingList: ArrayList<Upcoming>

    private lateinit var controller: UpcomingEpoxyController

    private val viewModel: TripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        upcomingBinding = FragmentUpcomingBinding.inflate(inflater, container, false)
        upcomingBinding.lifecycleOwner = this

        return upcomingBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = UpcomingEpoxyController()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        upcomingBinding.refreshUpcoming.setOnRefreshListener {
            setUpObservers()
        }
    }

    private fun setUpObservers(){
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        upcomingViewModel.bookings.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Resource.Status.LOADING ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = true
                }
                Resource.Status.SUCCESS ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = false
                    try {
                        if (it.data!!.isNotEmpty()){
                            upcomingList = ArrayList()
                            for (book in it.data){
                                if (book.user_id == getId && book.status == "booked"){
                                    upcomingList.add(Upcoming(book.trip_id,getTripTitle(book.id)!!, book.created_at))
                                }
                            }
                            setUpRecycler(upcomingList)
                        }
                    }catch (e: Exception){
                        Timber.e(e)
                    }
                }
                Resource.Status.ERROR ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = false
                }
            }
        })
    }

    private fun setUpRecycler(data: List<Upcoming>) {

        controller.setData(true, data)

        if (data.isNotEmpty()){
            successLayout()
        }else{
            errorLayout()
        }

        upcomingBinding.upcomingRecycler
                .setController(controller)

    }

    private fun successLayout(){
        upcomingBinding.errorLayout.visibility = View.GONE
        upcomingBinding.upcomingRecycler.visibility = View.VISIBLE
    }

    private fun errorLayout(){
        upcomingBinding.errorLayout.visibility = View.VISIBLE
        upcomingBinding.upcomingRecycler.visibility = View.GONE
    }

    private fun getTripTitle(id: Int) : String? {

        var title: String? = null

        viewModel.trips.observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    try {
                        for (trips in it.data!!){
                            if (trips.id == id) {
                               title = trips.start + " - " + trips.destination
                            }

                        }
                    }catch (e: IndexOutOfBoundsException){
                        Timber.e(e)
                    }
                }

                Resource.Status.ERROR ->
                    Timber.e("Error")

                Resource.Status.LOADING -> {

                }
            }
        })
        return title
    }

}