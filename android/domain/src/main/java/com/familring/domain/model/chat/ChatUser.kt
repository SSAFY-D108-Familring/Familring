package com.familring.domain.model.chat

data class ChatUser(
    val userId: Long = 0L,
    val userKakaoId: String = "",
    val userNickname: String = "",
    val userBirthDate: String = "",
    val userZodiacSign: String = "",
    val userRole: String = "",
    val userFace: String = "",
    val userColor: String = "",
    val userEmotion: String = "",
)
