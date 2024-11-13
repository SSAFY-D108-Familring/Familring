package com.familring.presentation.screen.interest

sealed interface InterestUiEvent {
    data object CreateSuccess : InterestUiEvent

    data object EditSuccess : InterestUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : InterestUiEvent
}
