package com.familring.domain.model.chat

import com.familring.domain.model.User
import java.time.LocalDateTime

data class Chat(
    val chatId: String,
    val roomId: String,
    val senderId: String,
    val sender: User,
    val content: String,
    val createdAt: LocalDateTime,
    val messageType: String,
    val vote: Vote?,
    val responseOfVote: String,
    val isVoteEnd: Boolean,
    val resultOfVote: ResultOfVote,
    val unReadMembers: Int,
)
