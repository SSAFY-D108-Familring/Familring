package com.familring.domain.model.calendar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleAttendance(
    val userId: Long = 0,
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
    val attendanceStatus: Boolean = false,
) : Parcelable

