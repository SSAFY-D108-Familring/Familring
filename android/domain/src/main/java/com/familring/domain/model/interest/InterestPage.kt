package com.familring.domain.model.interest

import com.google.gson.annotations.SerializedName

data class InterestPage(
    val pageNo: Int = 0,
    val hasNext: Boolean = false,
    val last: Boolean = true,
    @SerializedName("items")
    val selectedInterests: List<SelectedInterest> = listOf(),
)
