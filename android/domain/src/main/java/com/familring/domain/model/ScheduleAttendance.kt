package com.familring.domain.model

data class ScheduleAttendance(
    val userId: Long = 0,
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
    val isAttendance: Boolean = false,
)

