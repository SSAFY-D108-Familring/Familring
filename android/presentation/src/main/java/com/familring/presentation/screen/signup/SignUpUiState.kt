package com.familring.presentation.screen.signup

import java.io.File
import java.time.LocalDate

data class SignUpUiState(
    val isLoading: Boolean = false,
    val make: Boolean = true,
    val userKakaoId: String = "",
    val userNickname: String = "",
    val userBirthDate: LocalDate = LocalDate.now(),
    val userRole: String = "",
    val userColor: String = "",
    val userFace: File? = null,
    val familyCode: String = "",
    val userIsLunar: Boolean = false,
    val parents: List<String> = listOf(),
)
