package com.familring.presentation.screen.login

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent

    data class Error(
        val errorCode: String? = null,
        val errorMessage: String,
    ) : LoginEvent
}
