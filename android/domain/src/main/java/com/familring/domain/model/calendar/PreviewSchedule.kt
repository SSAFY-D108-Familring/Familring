package com.familring.domain.model.calendar

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class PreviewSchedule(
    val id: Long,
    val title: String,
    val color: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    var order: Int = -1,
) : Comparable<PreviewSchedule> {
    override fun compareTo(other: PreviewSchedule): Int =
        if (this.startTime.dayOfMonth == other.startTime.dayOfMonth) {
            this.getLength().compareTo(other.getLength()) * -1
        } else {
            this.startTime.compareTo(other.startTime)
        }

    private fun getLength(): Int = startTime.until(endTime, ChronoUnit.DAYS).toInt()
}
