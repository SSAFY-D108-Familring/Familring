package com.familring.domain.model.notification

data class NotificationResponse(
    val notificationId: Long,
    val receiverUserId: Long,
    val senderUserId: Long,
    val destinationId: String,
    val notificationType: String,
    val notificationTitle: String,
    val notificationMessage: String,
    val notificationIsRead: Boolean,
)
