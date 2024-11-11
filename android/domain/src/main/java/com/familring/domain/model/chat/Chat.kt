package com.familring.domain.model.chat

data class Chat(
    val chatId: String,
    val roomId: Long,
    val messageType: String,
    val senderId: Long,
    val sender: ChatUser,
    val content: String,
    val createdAt: String,
    val vote: Vote? = null,
    val responseOfVote: String,
    val resultOfVote: Map<String, Int> = emptyMap(),
    val unReadMembers: Int,
    val voteEnd: Boolean,
)
