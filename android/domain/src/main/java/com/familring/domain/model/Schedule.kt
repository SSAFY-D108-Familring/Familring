package com.familring.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Schedule(
    @SerializedName("id")
    val scheduleId: Long = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("startTime")
    val startTime: LocalDateTime = LocalDateTime.now(),
    @SerializedName("endTime")
    val endTime: LocalDateTime = LocalDateTime.now(),
    @SerializedName("hasTime")
    val hasTime: Boolean = false,
    @SerializedName("hasNotification")
    val hasNotification: Boolean = false,
    @SerializedName("hasAlbum")
    val hasAlbum: Boolean = false,
    @SerializedName("color")
    val backgroundColor: String = "",
    @SerializedName("userInfoResponses")
    val familyMembers: List<ScheduleAttendance> = listOf(),
)
