package app.mulipati.network.responses.account

data class User(
    val created_at: String,
    val email_verified_at: Any,
    val id: Int,
    val is_admin: Any,
    val name: String,
    val phone: String,
    val updated_at: String
)