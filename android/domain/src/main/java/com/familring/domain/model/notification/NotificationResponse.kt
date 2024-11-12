package com.familring.domain.model.notification

data class NotificationResponse(
    val notificationId: Long,
    val receiverUserId: Long,
    val senderUserId: Long,
    val notificationType: String,
    val notificationTitle: String,
    val notificationMessage: String,
    val notificationRead: Boolean,
)
