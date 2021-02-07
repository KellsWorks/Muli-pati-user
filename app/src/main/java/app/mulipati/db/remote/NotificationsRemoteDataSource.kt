package app.mulipati.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati.network.services.RemoteServices
import javax.inject.Inject


class NotificationsRemoteDataSource @Inject constructor(
   private val remoteServices: RemoteServices
): BaseDataSource() {

    suspend fun getNotifications() = getResult { remoteServices.getUserNotifications() }

}