package com.openclassrooms.hexagonal.games.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.openclassrooms.hexagonal.games.R


/**
 * FirebaseMessagingService implementation for handling push notifications.
 */
class NotificationsService : FirebaseMessagingService() {

    /**
     * Called when a new FCM message is received. (App opened case)
     * @param remoteMessage The FCM message received.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Extract the notification title and body from the message
        val title = remoteMessage.notification?.title ?: ""
        val body = remoteMessage.notification?.body ?: ""
        Log.d("FCM", "notification Message received: $title - $body")
        // Show the notification
        showNotification(title, body)
    }

    /**
     * Show a notification with the given title and body.
     * @param title The notification title.
     * @param body The notification body.
     */
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Setup the notification channel for devices running Android Oreo and over
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("HexagonalChannelID", "Firebase notification Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        //build the notification
        val notificationBuilder = NotificationCompat.Builder(this, "HexagonalChannelID")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notifications) // must be a drawable only !!!
            .setAutoCancel(true)

        //Show the notification
        notificationManager.notify(0, notificationBuilder.build())
    }

    /**
     * Called when a new token is generated.
     * @param token The new token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

}