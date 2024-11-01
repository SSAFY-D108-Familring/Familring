package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.request.UserLoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi {
    @POST("users/login")
    suspend fun login(
        @Body request: UserLoginRequest,
    ): BaseResponse<JwtToken>

    @Multipart
    @POST("users/join")
    suspend fun join(
        @Part("request") request: RequestBody,
        @Part userFace: MultipartBody.Part,
    ): BaseResponse<JwtToken>
}
