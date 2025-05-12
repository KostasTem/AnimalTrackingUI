package com.example.animaltrackingui.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

class NotificationManager(val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val messaging = Firebase.messaging

    private fun hasPermission(): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("MissingPermission")
    fun sendNotification(notificationChannel: NotificationChannel, notification: Notification) {
        if (hasPermission()) {
            notificationManager.notify(notificationChannel.NOTIFICATION_ID, notification)
        }
    }

    fun createChannels() {
        for (notificationChannel: NotificationChannel in channels) {
            val channel = NotificationChannelCompat.Builder(
                notificationChannel.CH_ID,
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
                .setName(notificationChannel.CH_NAME)
                .setDescription(notificationChannel.CH_DESC)
                .setVibrationEnabled(false)
                .setShowBadge(false)
                .build()

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun subscribeForNearbyEncounteredPosts() {
        messaging.subscribeToTopic("encountered_nearby")
    }

    fun subscribeForNearbyMissingPosts() {
        messaging.subscribeToTopic("missing_nearby")
    }

    fun unsubscribeForNearbyEncounteredPosts() {
        messaging.unsubscribeFromTopic("encountered_nearby")
    }

    fun unsubscribeForNearbyMissingPosts() {
        messaging.unsubscribeFromTopic("missing_nearby")
    }

    companion object {
        sealed class NotificationChannel(
            val NOTIFICATION_ID: Int,
            val CH_ID: String,
            val CH_NAME: String,
            val CH_DESC: String
        ) {
            object MissingNotificationChannel : NotificationChannel(
                5132,
                "5132",
                "Nearby Missing Animal Updates",
                "Get Updates For Nearby Missing Animals"
            )

            object EncounteredNotificationChannel : NotificationChannel(
                5133,
                "5133",
                "Nearby Encountered Animal Updates",
                "Get Updates For Nearby Encountered Animals"
            )

            object DefaultNotificationChannel : NotificationChannel(
                5134,
                "5134",
                "Default Notification Channel",
                "Used By Non Categorized Notifications"
            )
        }

        val channels = listOf(
            NotificationChannel.MissingNotificationChannel,
            NotificationChannel.EncounteredNotificationChannel
        )

    }

}