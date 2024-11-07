package com.familring.domain.model.calendar

import com.familring.domain.model.Profile
import com.google.gson.annotations.SerializedName

data class DailyLife(
    @SerializedName("id")
    val dailyId: Long = 0,
    val content: String = "",
    @SerializedName("photoUrl")
    val dailyImgUrl: String = "",
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
) {
    val profile: Profile
        get() =
            Profile(
                nickname = userNickname,
                zodiacImgUrl = userZodiacSign,
                backgroundColor = userColor,
            )
}
