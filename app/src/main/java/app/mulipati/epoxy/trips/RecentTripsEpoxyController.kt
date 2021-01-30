package app.mulipati.epoxy.trips

import android.content.Context
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

                        val tripDetails = parentView.image?.context?.getSharedPreferences("trip_details", Context.MODE_PRIVATE)
                                ?.edit()
                        tripDetails?.putInt("id", trip.id)
                        tripDetails?.putString("car_photo", trip.car_photo)
                        tripDetails?.putString("car_type", trip.car_type)
                        tripDetails?.putString("destination", trip.destination)
                        tripDetails?.putString("end_time", trip.end_time)
                        tripDetails?.putString("number_of_passengers", trip.number_of_passengers)
                        tripDetails?.putString("passenger_fare", trip.passenger_fare)
                        tripDetails?.putString("pick_up_place", trip.pick_up_place)
                        tripDetails?.putString("start", trip.start)
                        tripDetails?.putString("start_time", trip.start_time)
                        tripDetails?.putString("status", trip.status)

                        tripDetails?.apply()
                        parentView.image!!.findNavController().navigate(R.id.action_dashboardFragment_to_tripFragment)
                    }
                    .addTo(this)
            }
        }
    }
}