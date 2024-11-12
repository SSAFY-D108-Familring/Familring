package com.familring.presentation.screen.notification

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface NotificationEvent {
    @Immutable
    data object Loading : NotificationEvent

    @Immutable
    data object Success : NotificationEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : NotificationEvent
}
