package com.familring.presentation.screen.interest

sealed interface InterestUiEvent {
    data object CreateSuccess : InterestUiEvent

    data object EditSuccess : InterestUiEvent

    data class ShowDialog(
        val count: Int,
    ) : InterestUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : InterestUiEvent
}
