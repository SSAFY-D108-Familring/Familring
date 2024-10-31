package com.familring.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: Long,
    val userKakaoId: String,
    val nickname: String,
    val profileImageUrl: String,
) : Parcelable
