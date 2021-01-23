package app.mulipati.util

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