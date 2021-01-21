package app.mulipati.data.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    var token: String
)