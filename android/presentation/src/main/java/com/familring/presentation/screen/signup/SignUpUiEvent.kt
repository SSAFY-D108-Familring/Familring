package com.familring.presentation.screen.signup

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface SignUpUiEvent {
    @Immutable
    data object Loading : SignUpUiEvent

    @Immutable
    data object Success : SignUpUiEvent

    @Immutable
    data object MakeSuccess : SignUpUiEvent

    @Immutable
    data object JoinSuccess : SignUpUiEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : SignUpUiEvent
}
