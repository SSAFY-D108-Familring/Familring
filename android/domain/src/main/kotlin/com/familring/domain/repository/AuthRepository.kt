package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.request.UserLoginRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: UserLoginRequest): Flow<ApiResponse<JwtToken>>

    suspend fun login(): Flow<ApiResponse<Boolean>>
}
