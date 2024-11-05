package com.familring.domain.request

data class ScheduleCreateRequest(
    val title: String,
    val color: String,
    val hasNotification: Boolean,
    val hasTime: Boolean,
    val startTime: String,
    val endTime: String,
    val attendances: List<Attendance>,
) {
    data class Attendance(
        val userId: Long,
        val attendanceStatus: Boolean,
    )
}
