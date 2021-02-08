package app.mulipati.network

import app.mulipati.data.LocationResponse
import app.mulipati.data.User
import app.mulipati.data.auth.RegisterResponse
import app.mulipati.firebase.receiver.SendToken
import app.mulipati.network.responses.Basic
import app.mulipati.network.responses.account.AccountUpdateResponse
import app.mulipati.network.responses.account.Delete
import app.mulipati.network.responses.chats.MessageSent
import app.mulipati.network.responses.chats.MessagesResponse
import app.mulipati.network.responses.trips.BookingResponse
import app.mulipati.network.responses.trips.CancelResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Routes {

    //User routes

    @POST("v1/register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse?>?

    @POST("v1/login")
    @FormUrlEncoded
    fun login(
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): Call<User?>?

    @POST("v1/update-photo")
    @FormUrlEncoded
    fun photoUpdate(
        @Field("id") id: Int?,
        @Field("photo") photo: String?
    ): Call<app.mulipati.data.Response?>?

    @POST("v1/update-location")
    @FormUrlEncoded
    fun photoLocation(
        @Field("id") id: Int?,
        @Field("location") location: String?
    ): Call<LocationResponse?>?

    @POST("v1/update-account")
    @FormUrlEncoded
    fun accountUpdate(
            @Field("id") id: Int?,
            @Field("name") name: String?,
            @Field("email") email: String?,
           @Field("phone") phone: String?
    ): Call<AccountUpdateResponse?>?


    @POST("v1/delete")
    @FormUrlEncoded
    fun accountDelete(
            @Field("id") id: Int?
    ): Call<Delete?>?



    @POST("v1/fcm-token/save")
    @FormUrlEncoded
    fun sendToken(
            @Field("id") id: Int?,
            @Field("token") token: String?
    ): Call<SendToken?>?

    //Book trip
    @POST("v1/trips/book-trip")
    @FormUrlEncoded
    fun bookTrip(
            @Field("booker_id")booker_id: Int?,
            @Field("trip_id")trip_id: Int?
    ): Call<BookingResponse>

    @POST("v1/trips/cancel-trip")
    @FormUrlEncoded
    fun cancelTrip(
            @Field("id")id: Int?,
            @Field("trip_id")trip_id: Int?
    ): Call<CancelResponse>

    @POST("v1/trips/cancel-trip")
    @FormUrlEncoded
    fun deleteTrip(
            @Field("id")id: Int
    ): Call<Basic>

    //Messages
    @POST("v1/message/get-messages")
    @FormUrlEncoded
    fun tripMessages(
        @Field("toId")toId: Int?,
        @Field("fromId")fromId: Int?
    ): Call<MessagesResponse>

    @POST("v1/message/create")
    @FormUrlEncoded
    fun sendMessage(
            @Field("to") to: Int?,
            @Field("from") from: Int?,
            @Field("message") message: String?,
            @Field("time") time: String?
    ): Call<MessageSent>

    //Notifications routes
    @POST("v1/notifications/user-mark-notification")
    @FormUrlEncoded
    fun markAsRead(
        @Field("id") id: Int?
    ): Call<Basic?>?

    @POST("v1/notifications/user-notification-delete")
    @FormUrlEncoded
    fun deleteNotification(
        @Field("id") id: Int?
    ): Call<Basic?>?
}