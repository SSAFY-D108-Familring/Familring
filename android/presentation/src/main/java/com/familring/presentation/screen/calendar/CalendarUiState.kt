package com.familring.presentation.screen.calendar

import com.familring.domain.model.PreviewDaily
import com.familring.domain.model.PreviewSchedule

data class CalendarUiState(
    val previewSchedules: List<PreviewSchedule> = listOf(),
    val previewDailies: List<PreviewDaily> = listOf(),
)
