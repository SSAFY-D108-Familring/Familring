package com.familring.presentation.screen.question

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.question.QuestionList

@Stable
sealed interface QuestionListState {
    @Immutable
    data object Loading : QuestionListState

    @Immutable
    data class Success(
        val questionList: QuestionList
    ) : QuestionListState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : QuestionListState
}