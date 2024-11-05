package com.familring.domain.model

data class DaySchedule(
    val date: String,
    val schedules: List<PreviewSchedule>,
)
