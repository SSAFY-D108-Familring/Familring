package com.familring.domain.model.timecapsule

import com.familring.domain.model.Profile
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class TimeCapsuleMessage(
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
    @SerializedName("content")
    val message: String = "",
    @SerializedName("date")
    val createdAt: LocalDate = LocalDate.now(),
) {
    val profile: Profile
        get() =
            Profile(
                nickname = userNickname,
                zodiacImgUrl = userZodiacSign,
                backgroundColor = userColor,
            )
}
