package com.familring.domain.model.calendar

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
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
    @SerializedName("albumId")
    val albumId: Long? = null,
    @SerializedName("color")
    val backgroundColor: String = "",
    @SerializedName("userInfoResponses")
    val familyMembers: List<ScheduleAttendance> = listOf(),
) : Parcelable
