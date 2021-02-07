package app.mulipati.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati.network.services.RemoteServices
import javax.inject.Inject


class BookingsRemoteDataSource @Inject constructor(
   private val remoteServices: RemoteServices
): BaseDataSource() {

    suspend fun getBookings() = getResult { remoteServices.userBookings() }

}