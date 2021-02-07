package app.mulipati.network.services

import app.mulipati.network.responses.chats.MessagesResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MessagesService {

    @POST("v1/message/get-messages")
    @FormUrlEncoded
    suspend fun tripMessages(@Field("toId")toId: Int?, @Field("fromId")fromId: Int?): Response<MessagesResponse>
//
//    @POST("v1/message/create")
//    @FormUrlEncoded
//    suspend fun sendMessage(@Field("to") to: Int?, @Field("from") from: Int?, @Field("message") message: String?, @Field("time") time: String?): Response<MessageSent>

}