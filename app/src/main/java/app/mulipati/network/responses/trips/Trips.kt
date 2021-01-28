package app.mulipati.network.responses.trips


data class Trips(
    val car_photo: String,
    val car_type: String,
    val created_at: String,
    val destination: String,
    val end_time: String,
    val id: Int,
    val location: String,
    val number_of_passengers: String,
    val passenger_fare: String,
    val pick_up_place: String,
    val start: String,
    val start_time: String,
    val status: String,
    val updated_at: String,
    val user_id: Int
)