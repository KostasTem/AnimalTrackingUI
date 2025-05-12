package com.example.animaltrackingui.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.net.toUri
import com.example.animaltrackingui.AnimalTrackingApplication
import com.example.animaltrackingui.LocationProvider
import com.example.animaltrackingui.R
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class NotificationReceiver : FirebaseMessagingService() {

    private val app by lazy { applicationContext as AnimalTrackingApplication }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        scope.launch {
            val channel: NotificationChannel
            val resultIntent: Intent
            val ch: com.example.animaltrackingui.notifications.NotificationManager.Companion.NotificationChannel
            val user = app.container.appRepository.getUser()
            if(user != null){
                val username = message.data["username"]
                if(username != null && user.username == username && message.data["notification_type"]!="User"){
                    return@launch
                }
            }
            Log.i("Received Notification Of Type",message.data["notification_type"]!!)
            when (message.data["type"]) {
                "Encountered" -> {
                    ch =
                        com.example.animaltrackingui.notifications.NotificationManager.Companion.NotificationChannel.EncounteredNotificationChannel
                    channel = NotificationChannel(
                        ch.CH_ID,
                        ch.CH_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    resultIntent = Intent(
                        Intent.ACTION_VIEW,
                        "animal_tracking_app://encountered_post/${message.data["postID"]}".toUri()
                    )
                }

                "Missing" -> {
                    ch =
                        com.example.animaltrackingui.notifications.NotificationManager.Companion.NotificationChannel.MissingNotificationChannel
                    channel = NotificationChannel(
                        ch.CH_ID,
                        ch.CH_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    resultIntent = Intent(
                        Intent.ACTION_VIEW,
                        "animal_tracking_app://missing_post/${message.data["postID"]}".toUri()
                    )
                }

                else -> {
                    ch =
                        com.example.animaltrackingui.notifications.NotificationManager.Companion.NotificationChannel.DefaultNotificationChannel
                    channel = NotificationChannel(
                        ch.CH_ID,
                        ch.CH_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    resultIntent = Intent(Intent.ACTION_VIEW, "animal_tracking_app://main/".toUri())
                }
            }
            if (message.data["notification_type"] == "Post") {
                Log.i("Notification For Post Type", message.data["type"]!!)
                if (applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && Utils.hasNotificationPermission(
                        applicationContext
                    )
                ) {
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1).build()
                    LocationProvider.provider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken(){
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    }).addOnSuccessListener {
                        if(it == null){
                            Log.e("Notification Receiver Location", "Null")
                            return@addOnSuccessListener
                        }
                        var latitude: Double?
                        var longitude: Double?
                        try {
                            latitude = message.data["latitude"]?.toDouble()
                            longitude = message.data["longitude"]?.toDouble()
                        } catch (e: NumberFormatException) {
                            latitude = null
                            longitude = null
                        }
                        Log.i("User Location","${it.latitude} || ${it.longitude}")
                        if (latitude != null && longitude != null) {
                            val result = FloatArray(1)
                            android.location.Location.distanceBetween(
                                it.latitude,
                                it.longitude,
                                latitude,
                                longitude,
                                result
                            )
                            Log.i("Distance Between Locations",result[0].toString())
                            if (result[0] < 5000) {
                                sendNotification(
                                    channel = channel,
                                    resultIntent = resultIntent,
                                    contentText = message.data["body"],
                                    contentTitle = "An Animal Might Be Near You",
                                    ch = ch
                                )
                            }
                        }
                    }.addOnFailureListener {
                        it.localizedMessage?.let { it1 -> Log.e("Location Fetching Error", it1) }
                    }
                }
            } else if (message.data["notification_type"] == "User") {
                if (user != null && user.username == message.data["username"]) {
                    sendNotification(
                        channel = channel,
                        resultIntent = resultIntent,
                        contentText = message.data["body"],
                        contentTitle = "Post Update",
                        ch = ch
                    )
                }

            }
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String) {
        val appRepository = app.container.appRepository
        scope.launch {
            val device = appRepository.getDevice()
            if (device != null) {
                val jsonObject = JSONObject()
                jsonObject.put("oldID", device.deviceID)
                jsonObject.put("newID", token)
                device.deviceID = token
                appRepository.addDevice(device)
                val user = appRepository.getUser()
                if (user != null) {
                    val res = RetrofitObject.userAPI.updateDeviceID(jsonObject, user.token)
                    if (res.isSuccessful && res.body() != null) {
                        appRepository.setDeviceUploaded(true)
                    }
                }
            }
        }
    }

    private fun sendNotification(
        channel: NotificationChannel,
        resultIntent: Intent,
        contentText: String?,
        contentTitle: String,
        ch: com.example.animaltrackingui.notifications.NotificationManager.Companion.NotificationChannel
    ) {
        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val resultPendingIntent: PendingIntent? =
            TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
        val notification: Notification.Builder =
            Notification.Builder(this@NotificationReceiver, channel.id)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setSmallIcon(R.drawable.pet)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
        //notificationManager.createNotificationChannel(channel)
        //Send Notification
        app.container.notificationManager.sendNotification(
            ch,
            notification.build()
        )
    }
}