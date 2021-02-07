package app.mulipati.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun convertDate(month: Int): String{
    var monthName = ""
    when(month){
        1 ->{
            monthName = "January"
        }
        2 ->{
            monthName =  "February"
        }
        3 ->{
            monthName =  "March"
        }
        4 ->{
            monthName =  "April"
        }
        5 ->{
            monthName =  "May"
        }
        6 ->{
            monthName =  "June"
        }
        7 ->{
            monthName =  "July"
        }8 -> {
        monthName =  "August"
        }
        9 ->{
            monthName =  "September"
        }
        10 ->{
            monthName =  "October"
        }
        11 ->{
            monthName =  "November"
        }
        12 ->{
            monthName =  "December"
        }
    }
    return monthName
}

fun getDisplayDateTime(timeStamp: String): String{
    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = simpleDateFormat.parse(timeStamp)
        val convertDateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.getDefault())
        convertDateFormat.format(date!!)
    }catch (e: Exception){
        Timber.e(e)
        ""
    }
}

fun getDisplayDateTimeX(dateTime: String): String{
    return try {
        val simpleDateFormat = SimpleDateFormat("d-MM-yy hh:mm", Locale.getDefault())
        val date = simpleDateFormat.parse(dateTime)
        val convertDateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.getDefault())
        convertDateFormat.format(date!!)
    }catch (e: Exception){
        Timber.e(e)
        ""
    }
}



