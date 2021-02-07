package app.mulipati.network.responses.trips

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class UserBooking(
    val created_at: String,
    @PrimaryKey
    val id: Int,
    val status: String,
    val trip_id: Int,
    val updated_at: String,
    val user_id: Int
)