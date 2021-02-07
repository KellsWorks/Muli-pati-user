package app.mulipati.db.repositories

import app.mulipati.db.remote.NotificationsRemoteDataSource
import app.mulipati.util.performGetOperation
import app.mulipati.db.daos.NotificationsDao
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val remoteDataSource: NotificationsRemoteDataSource,
    private val localDataSource: NotificationsDao
) {

    fun getNotifications() = performGetOperation(
        databaseQuery = { localDataSource.getNotifications() },
        networkCall = { remoteDataSource.getNotifications() },
        saveCallResult = {

            localDataSource.deleteNotifications()
            localDataSource.insertNotifications(it.notifications)

        }
    )

}