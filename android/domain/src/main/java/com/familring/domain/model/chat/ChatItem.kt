package com.familring.domain.model.chat

data class ChatItem(
    val userId: Long = 0,
    val profileImg: String = "",
    val nickname: String = "",
    val color: String = "",
    val message: String = "",
)
