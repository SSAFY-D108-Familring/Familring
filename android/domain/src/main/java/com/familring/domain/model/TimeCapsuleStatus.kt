package com.familring.domain.model

import com.google.gson.annotations.SerializedName

data class TimeCapsuleStatus(
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("dayCount")
    val leftDays: Int? = null,
    @SerializedName("count")
    val timeCapsuleCount: Int? = null,
    @SerializedName("users")
    val writers: List<User>? = null,
)
