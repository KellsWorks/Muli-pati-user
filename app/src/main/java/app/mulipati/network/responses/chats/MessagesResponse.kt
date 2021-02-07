package app.mulipati.network.responses.chats

import app.mulipati.db.entities.Messages

data class MessagesResponse(
    val messages: List<Messages>
)