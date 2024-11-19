package com.familring.domain.model.interest

import com.google.gson.annotations.SerializedName

data class InterestCard(
    val userId: Long = 0,
    @SerializedName("userZodiacSign")
    val profileImgUrl: String = "",
    @SerializedName("userNickname")
    val nickname: String = "",
    @SerializedName("content")
    val interest: String = "",
)
