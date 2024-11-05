package com.familring.domain.model.calendar

import com.google.gson.annotations.SerializedName

data class MonthSchedulesDailies(
    @SerializedName("schedules")
    val previewSchedules: List<PreviewSchedule> = listOf(),
    @SerializedName("dailies")
    val previewDailies: List<PreviewDaily> = listOf(),
)
