package com.familring.domain.model.chat

import java.time.LocalDateTime

data class Vote(
    val voteId: String,
    val voteTitle: String,
    val voteMarkerId: Long,
    val familyCount: Int,
    val isCompleted: Boolean,
    val createdAt: LocalDateTime,
    val voteResult: ResultOfVote,
    val choices: ResultOfVote,
    val roomId: Long,
    val senderId: Long,
)
