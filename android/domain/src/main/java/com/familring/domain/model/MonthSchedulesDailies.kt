package com.familring.domain.model

import com.google.gson.annotations.SerializedName

data class MonthSchedulesDailies(
    @SerializedName("dailies")
    val previewDailies: List<PreviewDaily> = listOf(),
    @SerializedName("schedules")
    val previewSchedules: List<PreviewSchedule> = listOf(),
)
