package com.familring.domain.model.timecapsule

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TimeCapsule(
    @SerializedName("timeCapsuleId")
    val id: Int = 0,
    @SerializedName("date")
    val openDate: String = "",
    @SerializedName("items")
    val messages: List<TimeCapsuleMessage> = listOf(),
) {
    val leftDays = calcLeftDays(openDate)
}

private fun calcLeftDays(date: String): Int {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val openDate = LocalDate.parse(date, dateFormat)
    val today = LocalDate.now()

    val leftDays = today.until(openDate).days
    return leftDays
}
