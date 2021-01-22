package app.mulipati.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati.network.services.TripsService
import javax.inject.Inject


class TripRemoteDataSource @Inject constructor(
    private val tripService: TripsService
): BaseDataSource() {

    suspend fun getTrips() = getResult { tripService.getTrips() }
    suspend fun getTripsByLocation(location: String) = getResult { tripService.getTripsByLocation(location) }
}