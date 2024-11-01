package com.familring.presentation.screen.login

import com.kakao.sdk.auth.model.OAuthToken

sealed interface LoginState {
    data object Initial : LoginState

    data class Success(
        val token: OAuthToken,
        val userId: Long?,
    ) : LoginState

    data class NoRegistered(
        val message: String,
    ) : LoginState

    data class Error(
        val message: String,
    ) : LoginState
}
