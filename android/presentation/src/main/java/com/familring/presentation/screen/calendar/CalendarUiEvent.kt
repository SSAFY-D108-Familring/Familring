package com.familring.presentation.screen.calendar

sealed interface CalendarUiEvent {
    data object Loading : CalendarUiEvent

    data object DeleteSuccess : CalendarUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : CalendarUiEvent
}
