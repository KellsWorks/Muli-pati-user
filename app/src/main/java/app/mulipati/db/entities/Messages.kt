package app.mulipati.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Messages(
        @PrimaryKey
        var id: Int,
        var from: Int,
        var to: Int,
        var message: String,
        var time: String,
        var is_read: Int,
        var created_at: String,
        var updated_at: String
)