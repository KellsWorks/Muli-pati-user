package app.mulipati.helpers

import androidx.room.TypeConverter
import app.mulipati.network.responses.Trip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters  {
    @TypeConverter
    fun fromTrip(trip: List<Trip?>?): String? {
        if (trip == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Trip?>?>() {}.type
        return gson.toJson(trip, type)
    }

    @TypeConverter
    fun toTrip(trip: String?): List<Trip>? {
        if (trip == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Trip?>?>() {}.type
        return gson.fromJson<List<Trip>>(trip, type)
    }

    }