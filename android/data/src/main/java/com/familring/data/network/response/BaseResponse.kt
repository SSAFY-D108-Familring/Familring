package com.familring.data.network.response

import com.familring.data.exception.ApiException
import com.familring.data.exception.RefreshTokenExpiredException
import com.familring.domain.model.ApiResponse
import java.io.IOException

data class BaseResponse<T>(
    val statusCode: Int,
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
                        code = e.error.errorCode ?: "500",
                        message = e.error.errorMessage ?: "서버 내부 오류가 발생했습니다.",
                    )

                is RefreshTokenExpiredException ->
                    ApiResponse.Error.TokenError(
                        code = e.error.errorCode ?: "500",
                        message = e.error.errorMessage ?: "서버 내부 오류가 발생했습니다.",
                    )

                is IOException ->
                    ApiResponse.Error.NetworkError(
                        message = e.message ?: "Network Error",
                    )

                else ->
                    ApiResponse.Error.UnknownError(
                        message = e.message ?: "Unknown Error",
                    )
            }
        },
    )
