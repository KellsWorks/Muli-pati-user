package app.mulipati.epoxy.trips

import androidx.navigation.findNavController
import app.mulipati.R
import app.mulipati.network.responses.Trip
import com.airbnb.epoxy.Typed2EpoxyController

class RecentTripsEpoxyController: Typed2EpoxyController<Boolean?, List<Trip>>() {
    override fun buildModels(status: Boolean?, trips: List<Trip>?) {
        if (trips != null) {
            for (trip in trips){
                RecentTripsEpoxyModel_()
                    .id(trip.id)
                    .data(trip)
                    .click { _, parentView, _, _ ->
                        parentView.image!!.findNavController().navigate(R.id.action_dashboardFragment_to_tripFragment)
                    }
                    .addTo(this)
            }
        }
    }
}