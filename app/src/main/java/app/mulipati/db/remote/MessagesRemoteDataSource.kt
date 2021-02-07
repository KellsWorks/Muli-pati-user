package app.mulipati.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati.network.services.MessagesService
import javax.inject.Inject


class MessagesRemoteDataSource @Inject constructor(
    private val messagesService: MessagesService
): BaseDataSource() {

    suspend fun getMessages(to: Int, from: Int) = getResult {
        messagesService.tripMessages(to , from)
    }
}