package app.mulipati.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.mulipati.R
import app.mulipati.data.LocationResponse
import app.mulipati.data.RecentTrips
import app.mulipati.databinding.FragmentDashboardBinding
import app.mulipati.epoxy.trips.RecentTripsEpoxyController
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private lateinit var controller: RecentTripsEpoxyController

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

        bindLocation()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val trips = ArrayList<RecentTrips>()

        trips.add(RecentTrips(R.drawable.mazda_demio, "Banda's trip", "ZA - BT", "2 February - 10:00 PM"))
        trips.add(RecentTrips(R.drawable.mazda_demio, "Banda's trip", "ZA - BT", "2 February - 10:00 PM"))
        trips.add(RecentTrips(R.drawable.mazda_demio, "Banda's trip", "ZA - BT", "2 February - 10:00 PM"))

        controller = RecentTripsEpoxyController()
        controller.setData(true, trips)

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
                    }
                }
            }

        })
    }

}