package app.mulipati.network

import app.mulipati.firebase.receiver.SendToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class BackgroundServices{

     fun sendTokenToServer(token: String, id: Int){

        GlobalScope.launch(Dispatchers.IO) {
            val api = ApiClient.client!!.create(Routes::class.java)
            val sendToken = api.sendToken(id, token)

            sendToken!!.enqueue(object: Callback<SendToken?>{
                override fun onFailure(call: Call<SendToken?>, t: Throwable) {
                    Timber.e("Failed to send token to mulipati.com server")
                }

                override fun onResponse(call: Call<SendToken?>, response: Response<SendToken?>) {
                    when(response.code()){
                        200 ->{
                            Timber.e("Token sent to mulipati.com server")
                        }
                    }
                }

            })
        }
    }



}