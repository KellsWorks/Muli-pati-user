package app.mulipati.network.responses.account

data class Profile(
    val created_at: String,
    val email: String,
    val id: Int,
    val location: String,
    val photo: String,
    val role: String,
    val updated_at: String,
    val user_id: Int
)