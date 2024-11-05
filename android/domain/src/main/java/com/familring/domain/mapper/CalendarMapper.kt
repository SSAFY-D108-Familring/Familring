package com.familring.domain.mapper

import com.familring.domain.model.Profile
import com.familring.domain.model.ScheduleAttendance

fun ScheduleAttendance.toProfile(): Profile =
    Profile(
        nickName = userNickname,
        zodiacImgUrl = userZodiacSign,
        backgroundColor = userColor,
    )
