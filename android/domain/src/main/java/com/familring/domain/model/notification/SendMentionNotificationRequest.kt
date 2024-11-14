package com.familring.domain.model.notification

data class SendMentionNotificationRequest(
    val receiverId: Long,
    val mention: String,
)
