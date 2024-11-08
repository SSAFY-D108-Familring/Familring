package com.familring.domain.model.timecapsule

import com.google.gson.annotations.SerializedName

data class TimeCapsulePage(
    val pageNo: Int = 0,
    val last: Boolean = true,
    val hasNext: Boolean = false,
    @SerializedName("items")
    val timeCapsules: List<TimeCapsule> = listOf(),
)
