package app.mulipati.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal object ApiClient {

    const val BASE_URL = "http://192.168.43.254/larntech/api/"
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}