package com.familring.presentation.util

import java.time.LocalDate
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
): Boolean = year.length == 4 && (month.length in 1..2) && (date.length in 1..2)
