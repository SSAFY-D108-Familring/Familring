package com.familring.presentation.screen.interest

sealed interface OtherInterestUiEvent {
    data object Success : OtherInterestUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : OtherInterestUiEvent
}
