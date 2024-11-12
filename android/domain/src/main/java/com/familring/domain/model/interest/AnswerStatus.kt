package com.familring.domain.model.interest

import com.google.gson.annotations.SerializedName

data class AnswerStatus(
    @SerializedName("answerStatusMine")
    val isWroteInterest: Boolean = false,
    val content: String = "",
    @SerializedName("answerStatusFamily")
    val isFamilyWrote: Boolean = false,
)
