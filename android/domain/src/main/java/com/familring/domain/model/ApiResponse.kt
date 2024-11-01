package com.familring.domain.model

sealed class ApiResponse<out D> {
    data class Success<out D>(
        val data: D,
    ) : ApiResponse<D>()

    sealed class Error(
        open val code: String = "",
        open val message: String = "",
    ) : ApiResponse<Nothing>() {
        data class ServerError(
            override val code: String = "",
            override val message: String = "",
        ) : Error(code, message)

        data class TokenError(
            override val code: String = "",
            override val message: String = "",
        ) : Error(code, message)

        data class NetworkError(
            override val message: String,
        ) : Error(message = message)

        data class UnknownError(
            override val message: String,
        ) : Error(message = message)
    }
}
