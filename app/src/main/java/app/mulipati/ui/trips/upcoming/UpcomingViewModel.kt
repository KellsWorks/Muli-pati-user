package app.mulipati.ui.trips.upcoming

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.db.repositories.BookingsRepository
import app.mulipati.network.responses.trips.UserBooking
import app.mulipati.util.Resource

class UpcomingViewModel @ViewModelInject constructor(
        repository: BookingsRepository
) : ViewModel() {

    val bookings: LiveData<Resource<List<UserBooking>>> = repository.getBookings()

}