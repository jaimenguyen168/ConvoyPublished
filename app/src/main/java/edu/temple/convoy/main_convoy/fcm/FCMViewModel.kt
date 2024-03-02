package edu.temple.convoy.main_convoy.fcm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import edu.temple.convoy.main_convoy.location_data.LocationApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FCMViewModel : ViewModel() {
    private val _convoyParticipantsData = MutableLiveData<List<ConvoyParticipant>>()
    val convoyParticipantsData : LiveData<List<ConvoyParticipant>> = _convoyParticipantsData

    fun updateConvoyParticipantsData(convoyParticipants: List<ConvoyParticipant>) {
        viewModelScope.launch(Dispatchers.Main) {
            _convoyParticipantsData.value = convoyParticipants
        }
    }
}