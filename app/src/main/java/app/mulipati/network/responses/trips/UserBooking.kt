package app.mulipati.network.responses.trips

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bookings")
data class UserBooking(
    val created_at: String,
    @PrimaryKey
    val id: Int,
    val status: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("trip_id")
    val trip_id: Int,
    val updated_at: String,
    val user_id: Int
)