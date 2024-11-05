package com.familring.domain.model

import java.time.LocalDate

data class DaySchedule(
    val date: LocalDate,
    val schedules: List<PreviewSchedule>,
)
