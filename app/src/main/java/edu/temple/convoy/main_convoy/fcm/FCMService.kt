package edu.temple.convoy.main_convoy.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}