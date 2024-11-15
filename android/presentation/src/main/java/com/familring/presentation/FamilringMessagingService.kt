package com.familring.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FamilringMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM_TEST").d("New FCM Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.tag("FCM_TEST").d("Received FCM message: ${message.data}")

        val notificationType = message.data["type"] ?: "NONE"

        if (message.data.isNotEmpty()) {
            val title = message.data["title"] ?: "알림"
            val body = message.data["body"] ?: "새로운 메시지가 있습니다"
            showNotification(title, body, notificationType)
        } else if (message.notification != null) {
            val title = message.notification?.title ?: "알림"
            val body = message.notification?.body ?: "새로운 메시지가 있습니다"
            showNotification(title, body, notificationType)
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String,
    ) {
        val channelId = "knock_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent =
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                data = android.net.Uri.parse("familring://app/notification?type=$type")
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val channel =
            NotificationChannel(
                channelId,
                "알림",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "알림"
                enableLights(true)
                lightColor = Color.WHITE
                enableVibration(true)
            }
        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat
                .Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_fill_home)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
