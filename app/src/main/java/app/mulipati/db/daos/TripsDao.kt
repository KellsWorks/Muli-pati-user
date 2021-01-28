package app.mulipati.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati.network.responses.Trip
import app.mulipati.network.responses.trips.UserTrip

@Dao
interface TripsDao {

    @Query("SELECT * FROM trips")
    fun getAllTrips() : LiveData<List<Trip>>

    @Query("SELECT * FROM trips WHERE location = :location")
    fun getTrip(location: String): LiveData<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<Trip>)

    @Query("SELECT * FROM user_trips")
    fun getUserTrips() : LiveData<List<UserTrip>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserTrips(trips: List<UserTrip>)

    @Query("DELETE FROM user_trips")
    suspend fun deleteColleges()

}