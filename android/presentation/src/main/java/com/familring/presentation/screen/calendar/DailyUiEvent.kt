package com.familring.presentation.screen.calendar

interface DailyUiEvent {
    data object Success : DailyUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : DailyUiEvent
}
