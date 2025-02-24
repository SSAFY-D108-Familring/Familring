package com.familring.domain.model

data class JwtToken(
    val grantType: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val userId: Long = 0L,
)
