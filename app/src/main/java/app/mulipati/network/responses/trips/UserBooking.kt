package app.mulipati.network.responses.trips

data class UserBooking(
    val created_at: String,
    val id: Int,
    val status: String,
    val trip_id: Int,
    val updated_at: String,
    val user_id: Int
)