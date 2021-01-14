package app.mulipati.epoxy

import androidx.navigation.findNavController
import app.mulipati.R
import app.mulipati.data.RecentTrips
import com.airbnb.epoxy.Typed2EpoxyController

class RecentTripsEpoxyController: Typed2EpoxyController<Boolean?, List<RecentTrips>>() {
    override fun buildModels(status: Boolean?, trips: List<RecentTrips>?) {
        if (trips != null) {
            for (trip in trips){
                RecentTripsEpoxyModel_()
                    .id(trip.title)
                    .data(trip)
                    .click { _, parentView, _, _ ->
                        parentView.image!!.findNavController().navigate(R.id.action_dashboardFragment_to_tripFragment)
                    }
                    .addTo(this)
            }
        }
    }
}