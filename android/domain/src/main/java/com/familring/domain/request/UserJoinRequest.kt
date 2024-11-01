package com.familring.domain.request

data class UserJoinRequest(
    val userKakaoId: String,
    val userNickname: String,
    val userBirthDate: String,
    val userRole: UserRole,
    val userColor: String,
)

enum class UserRole(
    val value: String,
) {
    F("F"),
    M("M"),
    S("S"),
    D("D"),
}
