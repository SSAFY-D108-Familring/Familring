package com.familring.presentation.screen.question

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.question.QuestionAnswer

@Stable
sealed interface QuestionState {
    @Immutable
    data object Loading : QuestionState

    @Immutable
    data class Success(
        val questionId: Long,
        val questionContent: String,
        val answerContents: List<QuestionAnswer>,
    ) : QuestionState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : QuestionState
}
