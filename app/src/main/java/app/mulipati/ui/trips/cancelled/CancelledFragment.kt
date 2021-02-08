package app.mulipati.ui.trips.cancelled

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.mulipati.data.Cancelled
import app.mulipati.databinding.FragmentCancelledBinding
import app.mulipati.epoxy.cancelled.CancelledEpoxyController
import app.mulipati.ui.trips.upcoming.UpcomingViewModel
import app.mulipati.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CancelledFragment : Fragment() {

    private lateinit var cancelledBinding: FragmentCancelledBinding

    private lateinit var controller: CancelledEpoxyController

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var upcomingList: ArrayList<Cancelled>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cancelledBinding = FragmentCancelledBinding.inflate(inflater, container, false)
        cancelledBinding.lifecycleOwner = this

       return cancelledBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = CancelledEpoxyController()
        upcomingList = ArrayList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        cancelledBinding.refreshCancelled.setOnRefreshListener {
            setUpObservers()
        }

    }

    private fun setUpObservers(){
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        upcomingViewModel.bookings.observe(viewLifecycleOwner, Observer {
            upcomingList.clear()
            when(it.status){
                Resource.Status.LOADING ->{
                    cancelledBinding.refreshCancelled.isRefreshing = true
                }
                Resource.Status.SUCCESS ->{
                    cancelledBinding.refreshCancelled.isRefreshing = false
                    try {
                        if (it.data!!.isNotEmpty()){
                            for (book in it.data){
                                if (book.user_id == getId && book.status == "cancelled"){
                                    upcomingList.add(Cancelled(book.id, book.start + " - " + book.destination, book.created_at))
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
                    cancelledBinding.refreshCancelled.isRefreshing = false
                }
            }
        })
    }

    private fun setUpRecycler(data: List<Cancelled>) {

        controller.setData(true, data)

        if (data.isNotEmpty()){
            successLayout()
        }else{
            errorLayout()
        }

        cancelledBinding.cancelledRecycler
                .setController(controller)

    }

    private fun errorLayout(){
        cancelledBinding.errorLayout.visibility = View.VISIBLE
        cancelledBinding.cancelledRecycler.visibility = View.GONE
    }

    private fun successLayout(){
        cancelledBinding.errorLayout.visibility = View.GONE
        cancelledBinding.cancelledRecycler.visibility = View.VISIBLE
    }
}