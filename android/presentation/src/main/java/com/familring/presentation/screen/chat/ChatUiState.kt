package com.familring.presentation.screen.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface ChatUiState {
    @Immutable
    data object Loading : ChatUiState

    data class Success(
        val userId: Long = 0L,
    ) : ChatUiState

    @Immutable
    data class Error(
        val message: String,
    ) : ChatUiState
}
