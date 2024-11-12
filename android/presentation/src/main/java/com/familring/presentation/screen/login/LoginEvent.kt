package com.familring.presentation.screen.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginEvent {
    @Immutable
    data object None : LoginEvent

    @Immutable
    data object Loading : LoginEvent

    @Immutable
    data object LoginSuccess : LoginEvent

    data class Error(
        val errorCode: String? = null,
        val errorMessage: String,
    ) : LoginEvent
}
