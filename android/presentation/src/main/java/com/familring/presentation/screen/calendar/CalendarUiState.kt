package com.familring.presentation.screen.calendar

import com.familring.domain.model.DailyLife
import com.familring.domain.model.PreviewDaily
import com.familring.domain.model.PreviewSchedule
import com.familring.domain.model.Schedule

data class CalendarUiState(
    val previewSchedules: List<PreviewSchedule> = listOf(),
    val previewDailies: List<PreviewDaily> = listOf(),
    val detailedSchedule: List<Schedule> = listOf(),
    val detailedDailies: List<DailyLife> = listOf(),
)
