package com.familring.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun parseDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(dateString, formatter)
}
