package app.mulipati.ui.dashboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.mulipati.R
import app.mulipati.data.RecentTrips
import app.mulipati.databinding.FragmentDashboardBinding
import app.mulipati.epoxy.trips.RecentTripsEpoxyController
import timber.log.Timber

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
        Timber.d("not m")
    }

    private fun bindLocation(){
        val locationPrefs = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.districts,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        dashboardBinding.districtSelect.adapter = adapter

        dashboardBinding.districtSelect.setSelection(adapter.getPosition(locationPrefs?.getString("location", "")))
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

}