package com.familring.domain.request

import java.time.LocalDate

data class UserJoinRequest(
    val userKakaoId: String,
    val userNickname: String,
    val userBirthDate: LocalDate,
    val userRole: String,
    val userColor: String,
)
