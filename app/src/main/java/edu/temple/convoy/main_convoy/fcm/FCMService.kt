package edu.temple.convoy.main_convoy.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import edu.temple.convoy.MainActivity
import edu.temple.convoy.R
import edu.temple.convoy.ui.Constant

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
//            val convoyParticipants: List<ConvoyParticipant> = try {
//                Gson().fromJson(this, Array<ConvoyParticipant>::class.java).toList()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emptyList()
//            }

            Log.i("MESSAGE", this)
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

//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationId = 1
//        val requestCode = 1
//
//        val channelId = "Firebase Messaging ID"
//        val channelName = "Firebase Messaging"
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(
//                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            )
//        }
//
//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntentFlag = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) 0 else PendingIntent.FLAG_IMMUTABLE
//        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent, pendingIntentFlag)
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setContentTitle(message.notification?.title)
//            .setContentText(message.notification?.body)
//            .setSmallIcon(R.drawable.user)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        notificationManager.notify(notificationId, notification)
    }
}