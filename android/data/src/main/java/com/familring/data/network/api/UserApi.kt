package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.request.UserLoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users/login")
    suspend fun login(
        @Body request: UserLoginRequest,
    ): BaseResponse<JwtToken>
}
