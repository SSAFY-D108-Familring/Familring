package com.familring.domain.request

data class UserJoinRequest(
    val userKakaoId: String,
    val userNickname: String,
    val userBirthDate: String,
    val userRole: String,
    val userColor: String,
)
