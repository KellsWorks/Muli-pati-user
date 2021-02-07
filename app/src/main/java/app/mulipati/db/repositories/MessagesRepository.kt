package app.mulipati.db.repositories

import app.mulipati.db.daos.MessagesDao
import app.mulipati.db.remote.MessagesRemoteDataSource
import app.mulipati.util.performGetOperation
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val remoteDataSource: MessagesRemoteDataSource,
    private val localDataSource: MessagesDao
) {

    fun getMessages(from: Int, to: Int) = performGetOperation(
        databaseQuery = { localDataSource.getMessages(from, to)},
        networkCall = { remoteDataSource.getMessages(from, to)},
        saveCallResult = { localDataSource.insertMessages(it.messages) }
    )
}