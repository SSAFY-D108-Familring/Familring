package com.familring.presentation.screen.signup

import androidx.compose.runtime.Stable

@Stable
sealed interface SignUpUiEvent {
    data object Loading : SignUpUiEvent

    data object Success : SignUpUiEvent

    data object MakeSuccess : SignUpUiEvent

    data object JoinSuccess : SignUpUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : SignUpUiEvent
}
