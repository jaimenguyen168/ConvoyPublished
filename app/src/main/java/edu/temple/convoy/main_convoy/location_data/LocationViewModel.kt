package edu.temple.convoy.main_convoy.location_data

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.temple.convoy.login_flow.data.RetrofitClient
import edu.temple.convoy.login_flow.screen.showToast
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private var _location = MutableLiveData(LocationData(0.0, 0.0))
    val location = _location

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }
}