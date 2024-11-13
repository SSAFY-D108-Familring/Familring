package com.familring.domain.model.calendar

import java.time.LocalDateTime

data class PreviewSchedule(
    val id: Long,
    val title: String,
    val color: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    var order: Int = -1,
) : Comparable<PreviewSchedule> {
    override fun compareTo(other: PreviewSchedule): Int = this.startTime.compareTo(other.startTime)
}
