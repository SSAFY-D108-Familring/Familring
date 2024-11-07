package com.familring.domain.model.timecapsule

import com.familring.domain.model.Profile

data class TimeCapsuleMessage(
    val id: Int,
    val profile: Profile,
    val message: String,
    val createdAt: String,
)