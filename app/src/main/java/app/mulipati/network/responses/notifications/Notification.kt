package app.mulipati.network.responses.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    val content: String,
    val created_at: String,
    @PrimaryKey
    val id: Int,
    val status: String,
    val title: String,
    val updated_at: String,
    val user_id: Int
)