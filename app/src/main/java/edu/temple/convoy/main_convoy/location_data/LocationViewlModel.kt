package edu.temple.convoy.main_convoy.location_data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.temple.convoy.main_convoy.location_data.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel : ViewModel() {
    private val _location = MutableLiveData(LocationData(0.0, 0.0))
    val location = _location

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }
}