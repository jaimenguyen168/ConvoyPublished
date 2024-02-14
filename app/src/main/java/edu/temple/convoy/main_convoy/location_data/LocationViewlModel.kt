package edu.temple.convoy.main_convoy.location_data

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.temple.convoy.main_convoy.location_data.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _location = MutableStateFlow(LocationData(12.0, 12.0))
    val location : StateFlow<LocationData> = _location.asStateFlow()

    fun updateLocation(newLocation: LocationData) {
        viewModelScope.launch {
            _location.value = newLocation
        }
//        showLoc()
    }

    fun showLoc() {
        Log.i("Current Loc", location.value.toString())
    }
}