package edu.temple.convoy.main_convoy.fcm

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.serialization.json.Json

class FCMService : FirebaseMessagingService() {

    private val fcmViewModel: FCMViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FCMViewModel::class.java)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.get("payload")?.run {
            val messageReceived = Json.decodeFromString<MessageReceived>(this)

            Log.i("MESSAGE", messageReceived.data.toString())
//            fcmViewModel.updateConvoyParticipantsData(convoyParticipants)
        }

//        if (action == Constant.UPDATE) {
//            if (dataArray.isNullOrEmpty()) return
//
//            val convoyParticipants: List<ConvoyParticipant> = try {
//                Gson().fromJson(dataArray, Array<ConvoyParticipant>::class.java).toList()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emptyList()
//            }
//
//            fcmViewModel.updateConvoyParticipantsData(convoyParticipants)
//        }
    }
}