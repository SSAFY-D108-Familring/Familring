package com.familring.presentation.screen.login

import androidx.compose.runtime.Stable
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

    data class NoRegistered(
        val message: String,
    ) : LoginState

    @Immutable
    data class Error(
        val errorMessage: String = "",
    ) : LoginState
}
