package com.familring.domain.request

import java.time.LocalDateTime

data class ScheduleCreateRequest(
    val title: String,
    val color: String,
    val hasNotification: Boolean,
    val hasTime: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val attendances: List<Attendance>,
) {
    data class Attendance(
        val userId: Long,
        val attendanceStatus: Boolean,
    )
}
