package com.familring.presentation.screen.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface HomeEvent {
    @Immutable
    data object None : HomeEvent

    @Immutable
    data object Loading : HomeEvent

    @Immutable
    data object Success : HomeEvent

    @Immutable
    data object UpdateSuccess : HomeEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : HomeEvent
}
