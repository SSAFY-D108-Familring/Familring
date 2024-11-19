package com.familring.domain.model.chat

data class Vote(
    val voteId: String,
    val voteTitle: String,
    val voteMakerId: Long,
    val familyCount: Int,
    val createdAt: String,
    val voteResult: Map<String, Int>,
    val choices: Map<String, String>,
    val roomId: Long,
    val senderId: Long,
    val completed: Boolean,
)
