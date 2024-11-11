package com.familring.domain.model.chat

import com.familring.domain.model.User
import java.time.LocalDateTime

data class Chat(
    val chatId: String,
    val roomId: Long,
    val messageType: String,
    val senderId: Long,
    val sender: User,
    val content: String,
    val createdAt: LocalDateTime,
    val vote: Vote? = null,
    val responseOfVote: String,
    val resultOfVote: Map<String, Int> = emptyMap(),
    val unReadMembers: Int,
    val voteEnd: Boolean,
)
