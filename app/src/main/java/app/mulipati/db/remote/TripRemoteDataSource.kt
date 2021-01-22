package app.mulipati.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati.network.services.TripsService
import javax.inject.Inject


class TripRemoteDataSource @Inject constructor(
    private val tripService: TripsService
): BaseDataSource() {

    suspend fun getCharacters() = getResult { tripService.getTrips() }
    suspend fun getCharacter(id: Int) = getResult { tripService.getTrip(id) }
}