package app.mulipati.network.services

import app.mulipati.network.responses.TripsResponse
import retrofit2.Response
import retrofit2.http.GET

interface TripsService {

    @GET("v1/trips/trips/all")
    suspend fun getTrips() : Response<TripsResponse>
}