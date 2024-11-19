package com.familring.domain.model.chat

data class SendMessage(
    val roomId: String,
    val senderId: String,
    val content: String,
    val createdAt: String,
    val messageType: String,
    val voteTitle: String? = null,
)

data class VoteResponse(
    val roomId: String,
    val senderId: String,
    val voteId: String,
    val messageType: String,
    val responseOfVote: String,
)
