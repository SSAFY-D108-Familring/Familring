package com.familring.data.network.response

import com.familring.data.exception.ApiException
import com.familring.data.exception.RefreshTokenExpiredException
import com.familring.domain.model.ApiResponse
import java.io.IOException

data class BaseResponse<T>(
    val code: String,
    val data: T?,
    val message: String,
)

suspend fun <T> emitApiResponse(
    apiResponse: suspend () -> BaseResponse<T>,
    default: T,
): ApiResponse<T> =
    runCatching {
        apiResponse()
    }.fold(
        onSuccess = { result ->
            ApiResponse.Success(data = result.data ?: default)
        },
        onFailure = { e ->
            when (e) {
                is ApiException ->
                    ApiResponse.Error.ServerError(
                        errorCode = e.error.timeStamp,
                        errorMessage = e.error.errorMessage,
                    )

                is RefreshTokenExpiredException ->
                    ApiResponse.Error.TokenError(
                        errorCode = e.error.timeStamp,
                        errorMessage = e.error.errorMessage,
                    )

                is IOException ->
                    ApiResponse.Error.NetworkError(
                        errorMessage = e.message ?: "Network Error",
                    )

                else ->
                    ApiResponse.Error.UnknownError(
                        errorMessage = e.message ?: "Unknown Error",
                    )
            }
        },
    )
