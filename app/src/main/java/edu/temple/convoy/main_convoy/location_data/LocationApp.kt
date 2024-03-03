package edu.temple.convoy.main_convoy.location_data

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import edu.temple.convoy.main_convoy.fcm.ConvoyParticipant
import edu.temple.convoy.main_convoy.fcm.MessageReceived

class LocationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "location",
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private var messageCallback: FCMCallback? = null
    interface FCMCallback {
        fun messageReceived(messageReceived: MessageReceived)
    }

    fun registerCallback (callback: FCMCallback?) {
        messageCallback = callback
    }

    fun getCallBack () : FCMCallback? {
        return messageCallback
    }
}