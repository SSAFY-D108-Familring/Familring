package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.TimeCapsule
import kotlinx.coroutines.flow.Flow

interface TimeCapsuleRepository {
    suspend fun getTimeCapsuleStatus(): Flow<ApiResponse<Unit>>

    suspend fun createTimeCapsule(timeCapsule: TimeCapsule): Flow<ApiResponse<Unit>>

    suspend fun createTimeCapsuleAnswer(): Flow<ApiResponse<Unit>>

    suspend fun getTimeCapsuleWriters(): Flow<ApiResponse<Unit>>

    suspend fun getTimeCapsules(): Flow<ApiResponse<List<TimeCapsule>>>

    suspend fun getTimeCapsuleAnswers(): Flow<ApiResponse<Unit>>
}
