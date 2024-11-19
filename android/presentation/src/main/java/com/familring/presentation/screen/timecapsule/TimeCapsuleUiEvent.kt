package com.familring.presentation.screen.timecapsule

sealed interface TimeCapsuleUiEvent {
    data object Success : TimeCapsuleUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : TimeCapsuleUiEvent
}
