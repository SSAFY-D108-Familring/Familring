package com.familring.domain.model.timecapsule

import com.familring.domain.model.Profile

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
