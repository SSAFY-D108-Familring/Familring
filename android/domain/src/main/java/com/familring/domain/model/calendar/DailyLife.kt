package com.familring.domain.model.calendar

import com.familring.domain.model.Profile

data class DailyLife(
    val dailyImgUrl: String,
    val content: String,
    val profile: Profile,
)
