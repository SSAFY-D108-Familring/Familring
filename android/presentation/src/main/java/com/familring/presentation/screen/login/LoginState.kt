package com.familring.presentation.screen.login

import androidx.compose.runtime.Stable
import com.kakao.sdk.auth.model.OAuthToken
import javax.annotation.concurrent.Immutable

@Stable
sealed interface LoginState {
    @Immutable
    data object Loading : LoginState

    @Immutable
    data class Success(
        val token: OAuthToken,
        val userId: Long?,
    ) : LoginState

    @Immutable
    data class NoRegistered(
        val message: String,
    ) : LoginState

    @Immutable
    data class Error(
        val errorCode: String = "",
        val errorMessage: String = "",
    ) : LoginState
}
