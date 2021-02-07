package app.mulipati.db.repositories

import app.mulipati.db.daos.TripsDao
import app.mulipati.db.remote.BookingsRemoteDataSource
import app.mulipati.util.performGetOperation
import javax.inject.Inject

class BookingsRepository @Inject constructor(
    private val remoteDataSource: BookingsRemoteDataSource,
    private val localDataSource: TripsDao
) {

    fun getBookings() = performGetOperation(
        databaseQuery = { localDataSource.getBookings()},
        networkCall = { remoteDataSource.getBookings() },
        saveCallResult = {

            localDataSource.deleteBookings()
            localDataSource.insertBookings(it.userBookings)

        }
    )

}