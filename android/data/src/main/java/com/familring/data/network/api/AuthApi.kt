package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.JwtToken
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/users/jwt")
    suspend fun getNewToken(
        @Body refreshToken: String,
    ): BaseResponse<JwtToken>
}
