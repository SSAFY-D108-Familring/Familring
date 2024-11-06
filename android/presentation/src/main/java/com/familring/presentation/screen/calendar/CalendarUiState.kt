package com.familring.presentation.screen.calendar

import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.model.calendar.PreviewDaily
import com.familring.domain.model.calendar.PreviewSchedule
import com.familring.domain.model.calendar.Schedule

data class CalendarUiState(
    val previewSchedules: List<PreviewSchedule> = listOf(),
    val previewDailies: List<PreviewDaily> = listOf(),
    val detailedSchedule: List<Schedule> = listOf(),
    val detailedDailies: List<DailyLife> = listOf(),
)
