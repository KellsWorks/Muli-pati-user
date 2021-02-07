package app.mulipati.network.services


import app.mulipati.network.responses.notifications.NotificationsResponse
import app.mulipati.network.responses.trips.Bookings
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RemoteServices {

    @POST("v1/notifications/user-notifications")
    suspend fun getUserNotifications() : Response<NotificationsResponse>

    @POST("v1/trips/user-bookings")
    suspend fun userBookings(): Response<Bookings>

}