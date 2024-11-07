package com.familring.domain.model.question

data class QuestionAnswer(
    val userId: Long,
    val answerId: Long,
    val userNickname: String,
    val userZodiacSign: String,
    val userColor: String,
    val answerContent: String,
    val answerStatus: Boolean,
)
