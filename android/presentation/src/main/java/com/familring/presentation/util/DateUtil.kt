package com.familring.presentation.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(this, formatter)
}

/***
 * 날짜 형식이 올바른지 확인하는 함수
 */
fun isDateFormValid(
    year: String,
    month: String,
    date: String,
): Boolean {
    try {
        if (year.length < 4) return false
        LocalDate.of(year.toInt(), month.toInt(), date.toInt())
    } catch (e: Exception) {
        return false
    }

    return true
}

/***
 * 날짜 형식이 올바른지 확인하는 함수 + 시간, 분
 */
fun isDateFormValid(
    year: String,
    month: String,
    date: String,
    hour: String,
    minute: String,
): Boolean {
    try {
        if (year.length < 4) return false
        LocalDateTime.of(year.toInt(), month.toInt(), date.toInt(), hour.toInt(), minute.toInt())
    } catch (e: Exception) {
        return false
    }

    return true
}

fun String.toTimeOnly(): String =
    try {
        val localDateTime = LocalDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
