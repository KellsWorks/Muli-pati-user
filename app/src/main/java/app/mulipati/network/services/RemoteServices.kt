package app.mulipati.network.services


import app.mulipati.network.responses.notifications.NotificationsResponse
import app.mulipati.network.responses.trips.Bookings
import app.mulipati.network.responses.users.UsersResponse
import retrofit2.Response
import retrofit2.http.POST

interface RemoteServices {

    @POST("v1/notifications/user-notifications")
    suspend fun getUserNotifications() : Response<NotificationsResponse>

    @POST("v1/trips/user-bookings")
    suspend fun userBookings(): Response<Bookings>

    @POST("v1/users")
    suspend fun getUsers() : Response<UsersResponse>

}