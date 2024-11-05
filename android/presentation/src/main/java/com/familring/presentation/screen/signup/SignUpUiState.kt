package com.familring.presentation.screen.signup

import java.io.File
import java.time.LocalDate

data class SignUpUiState(
    val make: Boolean = true,
    val userKakaoId: String = "",
    val userNickname: String = "",
    val userBirthDate: LocalDate = LocalDate.now(),
    val userRole: String = "",
    val userColor: String = "",
    val userFace: File? = null,
    val familyCode: String = "",
)
