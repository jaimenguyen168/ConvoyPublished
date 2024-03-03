package edu.temple.convoy.main_convoy.fcm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FCMViewModel : ViewModel() {
    private val _convoyParticipantsData = MutableLiveData<List<ConvoyParticipant>>()
    val convoyParticipantsData : LiveData<List<ConvoyParticipant>> = _convoyParticipantsData

    private val _convoyId = MutableLiveData<String>()
    val convoyId : LiveData<String> = _convoyId

    fun updateConvoyParticipantsData(convoyParticipants: List<ConvoyParticipant>) {
        viewModelScope.launch(Dispatchers.Main) {
            _convoyParticipantsData.value = convoyParticipants
        }
    }

    fun getConvoyId(convoyId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _convoyId.value = convoyId
        }
    }
}