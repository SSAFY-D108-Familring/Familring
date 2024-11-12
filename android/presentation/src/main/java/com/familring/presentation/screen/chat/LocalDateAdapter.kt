package com.familring.presentation.screen.chat

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @FromJson
    fun fromJson(dateString: String?): LocalDate =
        try {
            dateString?.let { LocalDate.parse(it, formatter) } ?: LocalDate.now()
        } catch (e: Exception) {
            LocalDate.now() // 파싱 실패시 현재 날짜 반환
        }

    @ToJson
    fun toJson(date: LocalDate?): String = date?.format(formatter) ?: LocalDate.now().format(formatter)
}

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @FromJson
    fun fromJson(value: String?): LocalDateTime =
        try {
            value?.let { LocalDateTime.parse(it, formatter) } ?: LocalDateTime.now()
        } catch (e: Exception) {
            LocalDateTime.now()
        }

    @ToJson
    fun toJson(dateTime: LocalDateTime?): String = dateTime?.format(formatter) ?: LocalDateTime.now().format(formatter)
}

