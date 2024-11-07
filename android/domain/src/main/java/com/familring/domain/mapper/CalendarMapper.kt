package com.familring.domain.mapper

import com.familring.domain.model.Profile
import com.familring.domain.model.calendar.ScheduleAttendance

fun ScheduleAttendance.toProfile(): Profile =
    Profile(
        nickname = userNickname,
        zodiacImgUrl = userZodiacSign,
        backgroundColor = userColor,
    )
