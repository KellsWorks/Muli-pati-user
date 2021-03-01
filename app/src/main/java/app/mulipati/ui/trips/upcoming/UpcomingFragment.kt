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
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private var upcomingBinding: FragmentUpcomingBinding by autoCleared()

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var upcomingList: ArrayList<Upcoming>

    private lateinit var controller: UpcomingEpoxyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = UpcomingEpoxyController()
        upcomingList = ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        upcomingBinding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return upcomingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        upcomingBinding.refreshUpcoming.setOnRefreshListener {
            setUpObservers()
        }

    }

    private fun setUpObservers(){
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        upcomingViewModel.bookings.observe(viewLifecycleOwner, Observer {
            upcomingList.clear()
            when(it.status){
                Resource.Status.LOADING ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = true
                }
                Resource.Status.SUCCESS ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = false
                    try {

                        if (it.data!!.isNotEmpty()){
                            for (book in it.data){
                                Timber.e(getId.toString())
                                if (book.user_id == getId && book.status == "booked"){
                                    upcomingList.add(Upcoming(book.trip_id, book.start + " - " + book.destination, book.created_at))
                                }
                            }
                            setUpRecycler(upcomingList)
                        }else{
                            errorLayout()
                        }
                    }catch (e: Exception){
                        Timber.e(e)
                    }
                }
                Resource.Status.ERROR ->{
                    upcomingBinding.refreshUpcoming.isRefreshing = false
                    Timber.e("Error loading upcoming")
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


}