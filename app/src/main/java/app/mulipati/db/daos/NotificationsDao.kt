package app.mulipati.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati.network.responses.notifications.Notification

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM notifications")
    fun getNotifications() : LiveData<List<Notification>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(trips: List<Notification>)

    @Query("DELETE FROM notifications")
    fun deleteNotifications()
}