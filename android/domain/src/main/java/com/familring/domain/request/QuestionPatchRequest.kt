package com.familring.domain.request

import java.time.LocalDate

data class QuestionPatchRequest(
    val content: String,
    val modifiedAt: LocalDate = LocalDate.now(),
)
