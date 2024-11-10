package com.familring.presentation.screen.calendar

sealed interface CalendarUiEvent {
    data object DeleteSuccess : CalendarUiEvent

    data object CreateAlbumSuccess : CalendarUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : CalendarUiEvent
}
