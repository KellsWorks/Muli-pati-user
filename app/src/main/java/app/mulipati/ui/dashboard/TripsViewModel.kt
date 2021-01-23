package app.mulipati.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.switchMap
import app.mulipati.db.repositories.TripsRepository
import app.mulipati.network.responses.Trip
import app.mulipati.util.Resource

class TripsViewModel @ViewModelInject constructor(
    private val repository: TripsRepository
) : ViewModel() {

    val trips: LiveData<Resource<List<Trip>>> = repository.getTrips()

//    private val _location = MutableLiveData<String>()
//
//    private val _trips = _location.switchMap { location ->
//        repository.getTripsByLocation(location)
//    }
//    val trips: LiveData<Resource<Trip>> = _trips
//
//    fun start(location: String) {
//        _location.value = location
//    }

}