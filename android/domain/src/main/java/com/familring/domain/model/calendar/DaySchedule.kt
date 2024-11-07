package com.familring.domain.model.calendar

import java.time.LocalDate

data class DaySchedule(
    val date: LocalDate = LocalDate.now(),
    val schedules: List<PreviewSchedule> = listOf(),
    val dailies: List<PreviewDaily> = listOf(),
)
