package com.familring.presentation.screen.question

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface QuestionEvent {
    @Immutable
    data object Loading : QuestionEvent

    @Immutable
    data object Success : QuestionEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : QuestionEvent
}
