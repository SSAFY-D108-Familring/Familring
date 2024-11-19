package com.familring.presentation.screen.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface ChatUiEvent {
    @Immutable
    data class VoteError(
        val message: String,
    ) : ChatUiEvent

    @Immutable
    data object Init : ChatUiEvent

    @Immutable
    data object NewMessage : ChatUiEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : ChatUiEvent
}
