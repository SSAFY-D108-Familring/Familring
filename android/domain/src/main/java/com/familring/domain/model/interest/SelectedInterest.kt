package com.familring.domain.model.interest

import com.google.gson.annotations.SerializedName

data class SelectedInterest(
    val interestId: Long = 0,
    val index: Int = 0,
    val userNickname: String = "",
    @SerializedName("content")
    val interest: String = "",
)
