package app.mulipati.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati.R
import app.mulipati.data.RecentTrips
import app.mulipati.databinding.FragmentDashboardBinding
import app.mulipati.epoxy.RecentTripsEpoxyController

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