package app.mulipati.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.db.repositories.TripsRepository
import app.mulipati.network.responses.Trip
import app.mulipati.util.Resource

class TripsViewModel @ViewModelInject constructor(
    repository: TripsRepository
) : ViewModel() {

    val trips: LiveData<Resource<List<Trip>>> = repository.getTrips()

}