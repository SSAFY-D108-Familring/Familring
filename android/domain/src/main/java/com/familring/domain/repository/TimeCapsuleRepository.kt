package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.timecapsule.TimeCapsulePage
import com.familring.domain.model.timecapsule.TimeCapsuleStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TimeCapsuleRepository {
    suspend fun getTimeCapsuleStatus(): Flow<ApiResponse<TimeCapsuleStatus>>

    suspend fun createTimeCapsule(openDate: LocalDate): Flow<ApiResponse<Unit>>

    suspend fun createTimeCapsuleAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun getTimeCapsules(pageNo: Int): Flow<ApiResponse<TimeCapsulePage>>
}
