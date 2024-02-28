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

//    private val fcmViewModel: FCMViewModel by lazy {
//        ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FCMViewModel::class.java)
//    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val handler = Handler(Looper.getMainLooper())

        message.data.get("payload")?.run {
            val messageReceived = Json.decodeFromString<MessageReceived>(this)

            Log.i("MESSAGE", messageReceived.data.toString())

            (application as LocationApp).getCallBack()?.run {
                messageReceived.data?.let {
                    messageReceived(it)
                }
            }

//            handler.post {
//                messageReceived.data?.let { fcmViewModel.updateConvoyParticipantsData(it) }
//            }
//            messageReceived.data?.let { data ->
//                handler.post {
//                    fcmViewModel.updateConvoyParticipantsData(data)
//                }
//            }
        }
    }
}