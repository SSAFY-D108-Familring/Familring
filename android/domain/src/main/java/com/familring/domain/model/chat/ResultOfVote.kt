package com.familring.domain.model.chat

import com.google.gson.annotations.SerializedName

data class ResultOfVote(
    @SerializedName("찬성")
    val agree: Int,
    @SerializedName("반대")
    val disAgree: Int,
)
