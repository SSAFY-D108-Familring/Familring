package com.familring.domain.model

data class QuestionList(
    val pageNo: Int = 0,
    val hasNext: Boolean = false,
    val items: List<QuestionResponse> = emptyList(),
    val last: Boolean = false,
)
