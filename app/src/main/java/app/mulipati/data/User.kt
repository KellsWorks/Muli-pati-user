package app.mulipati.data

data class User(
    val id: Int,
    val membership: String,
    val name: String,
    val phone: String,
    val profile: List<Profile>,
    val token: String
)