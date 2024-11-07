package com.familring.presentation.screen.timecapsule

sealed interface TimeCapsuleCreateUiEvent {
    data object Success : TimeCapsuleCreateUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : TimeCapsuleCreateUiEvent
}
