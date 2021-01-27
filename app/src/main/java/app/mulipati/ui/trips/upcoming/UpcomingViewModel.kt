package app.mulipati.ui.trips.upcoming

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import app.mulipati.data.Cancelled

class UpcomingViewModel: ViewModel() {
    var upcomingCount: Int? = null
    var tripsArrayList: ArrayList<Cancelled>? = null
}