package com.familring.presentation.screen.mypage

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface MyPageState {
    @Immutable
    data object Init : MyPageState

    @Immutable
    data object Success : MyPageState

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : MyPageState
}
