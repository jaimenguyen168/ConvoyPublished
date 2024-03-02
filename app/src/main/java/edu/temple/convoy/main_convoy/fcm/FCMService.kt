package edu.temple.convoy.main_convoy.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.temple.convoy.main_convoy.location_data.LocationApp
import kotlinx.serialization.json.Json

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.get("payload")?.run {
            val messageReceived = Json.decodeFromString<MessageReceived>(this)

            Log.i("MESSAGE", messageReceived.data.toString())

            (application as LocationApp).getCallBack()?.run {
                messageReceived.data?.let {
                    messageReceived(it)
                }
            }
        }
    }
}