package app.mulipati.ui.trips.upcoming

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.data.Cancelled
import app.mulipati.databinding.FragmentUpcomingBinding
import app.mulipati.epoxy.upcoming.UpcomingEpoxyController
import app.mulipati.network.responses.Trip
import app.mulipati.ui.dashboard.TripsViewModel
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private var upcomingBinding: FragmentUpcomingBinding by autoCleared()

    private lateinit var controller: UpcomingEpoxyController

    private val viewModel: TripsViewModel by viewModels()

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Cancelled>


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
        tripsList = ArrayList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        upcomingViewModel.upcomingCount = tripsList.count()
        
        val tripsPreferences = context?.getSharedPreferences("trips_count", Context.MODE_PRIVATE)?.edit()

        tripsPreferences?.putString("upcomingCount", upcomingViewModel.upcomingCount.toString())
        tripsPreferences?.apply()

    }

    private fun setUpRecycler(data: List<Cancelled>){
        controller.setData(true, data)
        upcomingBinding.upcomingRecycler.setController(controller)
        upcomingBinding.upcomingRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpObservers(){
            val id = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
            viewModel.userTrips.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when(it.status){
                    Resource.Status.SUCCESS ->{
                        if (it.data?.get(0)?.user_id == id && it.data?.get(0)?.status == "cancelled"){
                            tripsList = ArrayList()
                            val dataSet = id?.let { it1 -> getTripsData(it1) }
                            if (dataSet != null) {
                                for (trips in dataSet){
                                    tripsList.add(Cancelled((trips.start + " - " + trips.destination), trips.start_time))
                                }
                            }
                            setUpRecycler(tripsList)
                        }
                    }
                    Resource.Status.ERROR ->
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                    Resource.Status.LOADING -> {
                        Timber.e("loading user trips")
                    }
                }
            })
    }

    private fun getTripsData(tripId: Int): ArrayList<Trip>{
        var tripList = ArrayList<Trip>()
            viewModel.trips.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Resource.Status.SUCCESS -> {
                        if (it.data?.get(0)?.user_id == 2) {
                            tripList = ArrayList()
                            for (trips in it.data) {
                                if (trips.id == tripId) {
                                    tripList.add(
                                            Trip(
                                                    trips.car_photo, trips.car_type, trips.created_at, trips.destination, trips.end_time, trips.id, trips.location,
                                                    trips.number_of_passengers, trips.passenger_fare, trips.pick_up_place, trips.start, trips.start_time, trips.status,
                                                    trips.updated_at, trips.user_id
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Resource.Status.ERROR ->
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                    Resource.Status.LOADING -> {

                    }
                }
            })
        return tripList
    }
}