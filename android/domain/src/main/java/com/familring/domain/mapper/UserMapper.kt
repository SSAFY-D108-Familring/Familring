package com.familring.domain.mapper

import com.familring.domain.model.Profile
import com.familring.domain.model.User

fun User.toProfile(): Profile =
    Profile(
        nickName = userNickname,
        zodiacImgUrl = userZodiacSign,
        backgroundColor = userColor,
    )
