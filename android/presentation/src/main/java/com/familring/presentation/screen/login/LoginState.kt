package com.familring.presentation.screen.login

import javax.annotation.concurrent.Immutable

interface LoginState {
    @Immutable
    data object Loading : LoginState

    @Immutable
    data class Success(
        val accessToken: String = "",
        val refreshToken: String = "",
    ) : LoginState

    @Immutable
    data class Error(
        val errorMessage: String = "",
    ) : LoginState
}
