package com.familring.domain.model

data class QuestionResponse(
    val questionId: Long = 0,
    val questionContent: String = "",
    val items: List<QuestionAnswer> = emptyList(),
)
