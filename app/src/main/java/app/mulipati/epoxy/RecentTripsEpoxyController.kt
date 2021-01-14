package app.mulipati.epoxy

import app.mulipati.data.RecentTrips
import com.airbnb.epoxy.Typed2EpoxyController

class RecentTripsEpoxyController: Typed2EpoxyController<Boolean?, List<RecentTrips>>() {
    override fun buildModels(status: Boolean?, trips: List<RecentTrips>?) {
        if (trips != null) {
            for (trip in trips){
                RecentTripsEpoxyModel_()
                    .id(trip.title)
                    .data(trip)
                    .addTo(this)
            }
        }
    }
}