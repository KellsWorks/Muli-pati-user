package app.mulipati.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati.network.responses.Trip

@Dao
interface TripsDao {

    @Query("SELECT * FROM trips")
    fun getAllTrips() : LiveData<List<Trip>>

    @Query("SELECT * FROM trips WHERE location = :location")
    fun getTrip(location: String): LiveData<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Trip>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trips: Trip)


}