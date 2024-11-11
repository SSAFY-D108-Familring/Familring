package com.familring.presentation.screen.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.chat.Chat

@Stable
sealed interface ChatUiState {
    @Immutable
    data object Loading : ChatUiState

    data class Success(
        val userId: Long = 0L,
        val chatList: List<Chat> = emptyList(),
    ) : ChatUiState
}
