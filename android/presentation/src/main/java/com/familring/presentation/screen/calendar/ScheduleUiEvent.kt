package com.familring.presentation.screen.calendar

interface ScheduleUiEvent {
    data object Success : ScheduleUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : ScheduleUiEvent
}
