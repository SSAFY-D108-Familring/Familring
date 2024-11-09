package com.familring.domain.model.calendar

import android.os.Parcelable
import com.familring.domain.model.Profile
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyLife(
    @SerializedName("id")
    val dailyId: Long = 0,
    val content: String = "",
    @SerializedName("photoUrl")
    val dailyImgUrl: String = "",
    val userNickname: String = "",
    val userZodiacSign: String = "",
    val userColor: String = "",
    val myPost: Boolean = false,
) : Parcelable {
    val profile: Profile
        get() =
            Profile(
                nickname = userNickname,
                zodiacImgUrl = userZodiacSign,
                backgroundColor = userColor,
            )
}
