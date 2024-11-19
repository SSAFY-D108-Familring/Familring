package com.familring.domain.model.chat

data class ChatResponse(
    val hasNext: Boolean = false,
    val chatList: List<Chat> = emptyList(),
)
