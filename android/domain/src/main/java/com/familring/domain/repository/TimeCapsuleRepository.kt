package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.TimeCapsule
import com.familring.domain.model.TimeCapsuleStatus
import kotlinx.coroutines.flow.Flow

interface TimeCapsuleRepository {
    suspend fun getTimeCapsuleStatus(): Flow<ApiResponse<TimeCapsuleStatus>>

    suspend fun createTimeCapsule(openDate: String): Flow<ApiResponse<Unit>>

    suspend fun createTimeCapsuleAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun getTimeCapsules(): Flow<ApiResponse<List<TimeCapsule>>>
}
