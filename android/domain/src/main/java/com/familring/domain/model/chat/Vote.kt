package com.familring.domain.model.chat

import java.time.LocalDateTime

data class Vote(
    val voteId: String,
    val voteTitle: String,
    val voteMarkerId: Long,
    val familyCount: Int,
    val isCompleted: Boolean,
    val createdAt: LocalDateTime,
    val voteResult: Map<String, Int>,
    val choices: Map<String, Int>,
    val roomId: Long,
    val senderId: Long,
)
