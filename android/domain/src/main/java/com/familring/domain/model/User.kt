package com.familring.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class User(
    val userId: Long = 0L,
    val userKakaoId: String = "",
    val userNickname: String = "",
    val userBirthDate: LocalDate = LocalDate.now(),
    val userZodiacSign: String = "",
    val userRole: String = "",
    val userFace: String = "",
    val userColor: String = "",
    val userEmotion: String = "",
) : Parcelable
