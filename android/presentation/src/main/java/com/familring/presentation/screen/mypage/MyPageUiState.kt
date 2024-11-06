package com.familring.presentation.screen.mypage

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface MyPageUiState {
    @Immutable
    data object Loading : MyPageUiState

    @Immutable
    data class Success(
        val profileImage: String = "",
        val userNickname: String = "",
        val userRole: String = "",
        val userColor: String = "",
        val userBirthDate: String = "",
        val code: String = "",
    ) : MyPageUiState
}
