package app.mulipati.db.repositories

import app.mulipati.db.daos.TripsDao
import app.mulipati.db.remote.TripRemoteDataSource
import app.mulipati.util.performGetOperation
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val remoteDataSource: TripRemoteDataSource,
    private val localDataSource: TripsDao
) {

    fun getTripsByLocation(location: String) = performGetOperation(
        databaseQuery = { localDataSource.getTrip(location) },
        networkCall = { remoteDataSource.getTripsByLocation(location) },
        saveCallResult = { localDataSource.insertAll(it.trips) }
    )

    fun getTrips() = performGetOperation(
        databaseQuery = { localDataSource.getAllTrips() },
        networkCall = { remoteDataSource.getTrips() },
        saveCallResult = { localDataSource.insertAll(it.trips) }
    )

    fun getUserTrips() = performGetOperation(

            databaseQuery = { localDataSource.getUserTrips() },
            networkCall = { remoteDataSource.getUserTrips()},
            saveCallResult = { localDataSource.insertUserTrips(it.userTrips) }
    )
}