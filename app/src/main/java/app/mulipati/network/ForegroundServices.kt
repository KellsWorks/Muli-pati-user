package app.mulipati.network

import android.content.Context
import app.mulipati.network.responses.trips.Bookings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ForegroundServices(val context: Context){

    fun userBooking(tripId: Int, userId: Int){

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getId = apiClient.userBookings(userId)

        getId?.enqueue(object: Callback<Bookings?>{
            override fun onFailure(call: Call<Bookings?>, t: Throwable) {
                Timber.e(t)
            }

            override fun onResponse(call: Call<Bookings?>, response: Response<Bookings?>) {
                when(response.code()){
                    200 ->{
                       val bookings = response.body()!!.userBookings
                        for(book in bookings){
                            if (book.id == tripId){
                                val parsedID = context.getSharedPreferences("parsedID", Context.MODE_PRIVATE).edit()
                                parsedID.putInt("id", book.id)
                                parsedID.apply()

                            }
                        }

                    }
                }
            }

        })

    }

}