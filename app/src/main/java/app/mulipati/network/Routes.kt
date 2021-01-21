package app.mulipati.network

import app.mulipati.data.auth.Register
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST




interface Routes {

    @POST("register.php")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<Register?>?

}