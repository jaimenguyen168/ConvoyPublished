package edu.temple.convoy.main_convoy.location_data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private var _location = MutableLiveData(LocationData(0.0, 0.0))
    val location = _location

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }
}