package app.mulipati.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati.network.responses.users.AppUser

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    fun getUsers() : LiveData<List<AppUser>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<AppUser>)

    @Query("DELETE FROM users")
    fun deleteUsers()

}