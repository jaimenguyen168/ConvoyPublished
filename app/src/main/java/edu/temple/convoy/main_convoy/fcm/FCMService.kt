package edu.temple.convoy.main_convoy.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.temple.convoy.main_convoy.location_data.LocationApp
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val json = Json { ignoreUnknownKeys = true }
        message.data.get("payload")?.run {
            val messageReceived = json.decodeFromString<MessageReceived>(this)

            (application as LocationApp).getCallBack()?.run {
                messageReceived(messageReceived)
            }
        }
    }
}