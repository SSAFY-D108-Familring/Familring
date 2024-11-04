package com.familring.presentation.screen.signup

import java.io.File

data class SignUpUiState(
    val make: Boolean = true,
    val userKakaoId: String = "",
    val userNickname: String = "",
    val userBirthDate: String = "",
    val userRole: String = "",
    val userColor: String = "",
    val userFace: File? = null,
    val familyCode: String = "",
)
