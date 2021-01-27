package app.mulipati.network.services

import app.mulipati.network.responses.TripsResponse
import app.mulipati.network.responses.trips.UserTrips
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TripsService {

    @GET("v1/trips/trips/all")
    suspend fun getTrips() : Response<TripsResponse>

    @POST("v1/trips/user-trips")
    suspend fun getUserTrips(): Response<UserTrips>

    @GET("v1/trips/trips/all/{location}")
    suspend fun getTripsByLocation(@Path("location") location: String): Response<TripsResponse>

}