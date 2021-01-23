package app.mulipati.ui.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.mulipati.R
import app.mulipati.data.LocationResponse
import app.mulipati.databinding.FragmentDashboardBinding
import app.mulipati.epoxy.trips.RecentTripsEpoxyController
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.Trip
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import app.mulipati.util.convertDate
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DashboardFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var dashboardBinding: FragmentDashboardBinding by autoCleared()
    private lateinit var controller: RecentTripsEpoxyController

    private val viewModel: TripsViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Trip>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardBinding.lifecycleOwner = this

        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        arguments?.getString("location")?.let {
//            viewModel.start("Lilongwe") }
        setupObservers()
        bindLocation()

        dashboardBinding.pickDate.setOnClickListener {
            setUpTripDate()
        }

        val formattedDate = context?.getSharedPreferences("date", Context.MODE_PRIVATE)?.getString("date", "")

        if (formattedDate.isNullOrEmpty()){
            dashboardBinding.pickDate.text = "Set date"
        }else{
            dashboardBinding.pickDate.text = formattedDate
        }
    }

    private fun setUpTripDate(){

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.show()
    }


    private fun setupObservers() {
        val locationPrefs = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        viewModel.trips.observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data?.get(0)?.user_id ==  2){
                        tripsList = ArrayList()
                        for (trips in it.data){
                            if (trips.location == locationPrefs?.getString("location", "")?.toLowerCase(
                                    Locale.ROOT
                                )?.capitalize()
                            ) {

                                tripsList.add(
                                    Trip(
                                        trips.car_photo, trips.car_type, trips.created_at, trips.destination, trips.end_time, trips.id, trips.location,
                                        trips.number_of_passengers, trips.passenger_fare, trips.pick_up_place, trips.start, trips.start_time, trips.status,
                                        trips.updated_at, trips.user_id
                                    )
                                )

                                dashboardBinding.recentTripsRecycler.visibility = View.VISIBLE
                                dashboardBinding.noTrips.visibility = View.GONE
                                dashboardBinding.tripsMore.visibility = View.VISIBLE

                            }
                            else{
                                dashboardBinding.recentTripsRecycler.visibility = View.GONE
                                dashboardBinding.noTrips.visibility = View.VISIBLE
                                dashboardBinding.tripsMore.visibility = View.GONE
                            }
                        }

                        setUpRecycler(tripsList)
                    }
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {

                }
            }
        })
    }


    private fun bindLocation() {

        val locationPrefs = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.districts,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        dashboardBinding.districtSelect.adapter = adapter

        val location = adapter.getPosition(
            locationPrefs?.getString("location", "")?.toUpperCase(
                Locale.ROOT
            )
        )

//        Timber.e(location.toString())
//        dashboardBinding.districtSelect.setPromptId(location)

        dashboardBinding.districtSelect.setSelection(location)
        dashboardBinding.districtSelect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    updateLocation(dashboardBinding.districtSelect.selectedItem as String)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setUpRecycler(data: List<Trip>) {

        controller = RecentTripsEpoxyController()
        controller.setData(true, data)

        dashboardBinding.recentTripsRecycler
            .setController(controller)

    }

    private fun updateLocation(location: String){

        val api = ApiClient.client!!.create(Routes::class.java)

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()

        val upload: Call<LocationResponse?>? = api.photoLocation(user?.getInt("profile_id", 0), location)
        upload?.enqueue(object : Callback<LocationResponse?> {
            override fun onFailure(call: Call<LocationResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error updating location",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onResponse(call: Call<LocationResponse?>, response: retrofit2.Response<LocationResponse?>) {
                when (response.code()){
                    200 -> {
                        userPreferences?.putString("location", location)
                        userPreferences?.apply()

                        setupObservers()
                    }
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val m = month+1
        val mothName = convertDate(m)
        val formattedDate = "$dayOfMonth $mothName, $year"

        dashboardBinding.pickDate.text = formattedDate

        val date = context?.getSharedPreferences("date", Context.MODE_PRIVATE)?.edit()
        date?.putString("date", formattedDate)
        date?.apply()

        }
    }
