@file:Suppress("DEPRECATION")

package app.mulipati.ui.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.addCallback
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
import app.mulipati.util.*
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DashboardFragment : Fragment(), DatePickerDialog.OnDateSetListener, android.widget.SearchView.OnQueryTextListener {

    private var dashboardBinding: FragmentDashboardBinding by autoCleared()

    private lateinit var controller: RecentTripsEpoxyController

    private val viewModel: TripsViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Trip>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = RecentTripsEpoxyController()
        tripsList = ArrayList()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)

        dashboardBinding.lifecycleOwner = this

        return dashboardBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindLocation()

        dashboardBinding.pickDate.setOnClickListener {
            setUpTripDate()
        }

        val formattedDate = context?.getSharedPreferences("date", Context.MODE_PRIVATE)?.getString("date", "")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) - 1

        val todayDate = "$day " + convertDate(month) + ", $year"

        if (formattedDate.isNullOrEmpty()){
            dashboardBinding.pickDate.text = todayDate
        }else{
            dashboardBinding.pickDate.text = formattedDate
        }

        dashboardBinding.searchView.setOnQueryTextListener(this)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            val exitIntent = Intent(
                    Intent.ACTION_MAIN
            )
            exitIntent.addCategory(Intent.CATEGORY_HOME)
            exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            requireActivity().startActivity(
                    exitIntent
            )
        }

        setupObservers()
    }

    private fun setUpTripDate(){

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {

        val location = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("location", "")

        val dialog = ProgressDialog(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(false)
        dialog.setMessage("Loading trips... please wait")
        dialog.show()

        viewModel.trips.observe(viewLifecycleOwner, Observer {
            tripsList.clear()
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    dialog.dismiss()
                   try {
                           if(it.data!!.isNotEmpty()){
                               for (trips in it.data){
                                   if (location != null) {
                                       if (trips.location.toUpperCase(Locale.ROOT) == location
                                       ) {
                                           tripsList.add(
                                                   Trip(
                                                           trips.car_photo, trips.car_type, trips.created_at, trips.destination, trips.end_time, trips.id, trips.location,
                                                           trips.number_of_passengers, trips.passenger_fare, trips.pick_up_place, trips.start, getDisplayDateTimeX(trips.start_time), trips.status,
                                                           getDisplayDateTime(trips.updated_at), trips.user_id
                                                   )
                                           )
                                       }
                                   }

                               }
                               setUpRecycler(tripsList)
                               val tripsCount = tripsList.count()
                               dashboardBinding.tripsMore.text = "($tripsCount)"
                           }


                   }catch (e: IndexOutOfBoundsException){
                       Timber.e(e)
                   }
                }

                Resource.Status.ERROR ->
                {
                    dialog.dismiss()
                }

                Resource.Status.LOADING -> {
                    dialog.show()
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


        dashboardBinding.districtSelect.setSelection(location)
        try {
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
        }catch (e: IllegalArgumentException){
            Timber.e(e)
        }
    }

    private fun setUpRecycler(data: List<Trip>) {

        if (data.count() == 0){
            dashboardBinding.recentTripsRecycler.visibility = View.GONE
            dashboardBinding.noTrips.visibility = View.VISIBLE
            dashboardBinding.tripsMore.visibility = View.GONE
        }
        else{
            dashboardBinding.recentTripsRecycler.visibility = View.VISIBLE
            dashboardBinding.noTrips.visibility = View.GONE
            dashboardBinding.tripsMore.visibility = View.VISIBLE
        }

        controller = RecentTripsEpoxyController()
        controller.setData(true, data)

        dashboardBinding.recentTripsRecycler.setController(controller)

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

    private fun filter(models: List<Trip>, query: String): List<Trip> {
        val lowerCaseQuery = query.toLowerCase(Locale.ROOT)
        val filteredModelList: MutableList<Trip> = ArrayList()
        for (model in models) {
            val text: String = model.destination.toLowerCase(Locale.ROOT)
            val rank: String = java.lang.String.valueOf(model.id)
            if (text.contains(lowerCaseQuery) || rank.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        val filterModelList: List<Trip> = filter(tripsList, query!!)

        controller.setData(true, filterModelList)

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText == ""){
            controller.setData(true, tripsList)
        }

        return true
    }

    override fun onDestroyView() {
        viewModel.trips.removeObservers(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewModel.trips.removeObservers(this)
        super.onDestroy()
    }

}
