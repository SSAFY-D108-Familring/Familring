package com.familring.domain.model

import com.google.gson.annotations.SerializedName

data class TimeCapsuleMessage(
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
    @SerializedName("content")
    val message: String = "",
    @SerializedName("date")
    val createdAt: String = "",
) {
    val profile =
        Profile(
            nickname = userNickname,
            zodiacImgUrl = userZodiacSign,
            backgroundColor = userColor,
        )
}
