package com.familring.domain.model.timecapsule

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class TimeCapsule(
    @SerializedName("timeCapsuleId")
    val id: Int = 0,
    @SerializedName("date")
    val openDate: LocalDate = LocalDate.now(),
    @SerializedName("items")
    val messages: List<TimeCapsuleMessage> = listOf(),
) {
    val leftDays: Int
        get() =
            calcLeftDays(openDate)
}

private fun calcLeftDays(openDate: LocalDate): Int {
    val today = LocalDate.now()

    val leftDays = today.until(openDate).days
    return leftDays
}
