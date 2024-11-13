package com.familring.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
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
        // 수신된 메시지 로그로 출력
        Timber.tag("FCM_TEST").d("Received FCM message: ${message.data}")

        // data payload가 있는 경우
        if (message.data.isNotEmpty()) {
            val title = message.data["title"] ?: "알림"
            val body = message.data["body"] ?: "새로운 메시지가 있습니다"
            showNotification(title, body)
        }
        // notification payload가 있는 경우
        else if (message.notification != null) {
            val title = message.notification?.title ?: "알림"
            val body = message.notification?.body ?: "새로운 메시지가 있습니다"
            showNotification(title, body)
        }
    }

    private fun showNotification(
        title: String,
        body: String,
    ) {
        val channelId = "knock_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo 이상에서는 채널 생성 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    "똑똑 알림",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "답변 독촉 알림"
                    enableLights(true)
                    lightColor = Color.WHITE
                    enableVibration(true)
                }
            notificationManager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat
                .Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_home) // 알림 아이콘 필요
                .setAutoCancel(true)
                .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
