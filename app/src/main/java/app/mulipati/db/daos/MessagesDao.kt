package app.mulipati.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati.db.entities.Messages


@Dao
interface MessagesDao {

    @Query("SELECT * FROM messages where `from` = :from and `to` = :to")
    fun getMessages(from: Int, to: Int) : LiveData<List<Messages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Messages>)

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()

}