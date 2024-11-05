package com.familring.domain.model

import java.time.LocalDateTime

data class PreviewSchedule(
    val id: Long,
    val title: String,
    val color: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
