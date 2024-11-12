package com.familring.domain.model.interest

import com.google.gson.annotations.SerializedName

data class Mission(
    @SerializedName("photoUrl")
    val missionImgUrl: String = "",
    val userNickname: String = "",
    @SerializedName("userZodiacSign")
    val profileImgUrl: String = "",
)
