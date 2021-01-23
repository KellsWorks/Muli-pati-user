package app.mulipati.network.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "trips"
)
data class Trip(

    @SerializedName("car_photo")
    val car_photo: String,

    @SerializedName("car_type")
    val car_type: String,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("destination")
    val destination: String,

    @SerializedName("end_time")
    val end_time: String,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("location")
    val location: String,

    @SerializedName("number_of_passengers")
    val number_of_passengers: String,

    @SerializedName("passenger_fare")
    val passenger_fare: String,

    @SerializedName("pick_up_place")
    val pick_up_place: String,

    @SerializedName("start")
    val start: String,

    @SerializedName("start_time")
    val start_time: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("updated_at")
    val updated_at: String,

    @SerializedName("user_id")
    val user_id: Int
)