package com.familring.domain.model

data class TimeCapsuleStatus(
    val status: Int = 0,
    val leftDays: Int? = null,
    val timeCapsuleCount: Int? = null,
    val writers: List<Profile>? = null,
)
