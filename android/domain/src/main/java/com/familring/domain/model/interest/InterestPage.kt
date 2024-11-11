package com.familring.domain.model.interest

data class InterestPage(
    val pageNo: Int = 0,
    val hasNext: Boolean = false,
    val last: Boolean = true,
    val items: List<SelectedInterest> = listOf(),
)
