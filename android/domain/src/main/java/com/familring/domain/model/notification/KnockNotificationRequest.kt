package com.familring.domain.model.notification

data class KnockNotificationRequest(
    val targetUserId: String,
    val senderNickname: String,
)
