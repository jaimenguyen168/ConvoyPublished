package edu.temple.convoy.main_convoy.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class FCMViewModel : ViewModel() {
    private val _convoyParticipantsData = MutableLiveData<List<ConvoyParticipant>>()
    val convoyParticipantsData: LiveData<List<ConvoyParticipant>> = _convoyParticipantsData

    fun updateConvoyParticipantsData(convoyParticipants: List<ConvoyParticipant>) {
        _convoyParticipantsData.value = convoyParticipants
    }
}