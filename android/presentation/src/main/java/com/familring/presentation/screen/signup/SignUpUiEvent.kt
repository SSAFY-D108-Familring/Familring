package com.familring.presentation.screen.signup

import androidx.compose.runtime.Stable

@Stable
sealed interface SignUpUiEvent {
    data object Success : SignUpUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : SignUpUiEvent
}
