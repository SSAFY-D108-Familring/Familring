package com.familring.presentation.screen.login

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    data class Error(
        val errorCode: String? = null,
        val errorMessage: String
    ) : LoginEvent()
}