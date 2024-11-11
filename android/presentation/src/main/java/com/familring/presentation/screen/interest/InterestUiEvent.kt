package com.familring.presentation.screen.interest

sealed interface InterestUiEvent {
    data object Success : InterestUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : InterestUiEvent
}
