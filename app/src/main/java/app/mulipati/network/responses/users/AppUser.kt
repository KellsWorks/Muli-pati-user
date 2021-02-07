package app.mulipati.network.responses.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class AppUser(
    @PrimaryKey
    val id: Int,
    val name: String
)