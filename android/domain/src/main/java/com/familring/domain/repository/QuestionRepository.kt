package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.QuestionResponse
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun getQuestion(): Flow<ApiResponse<QuestionResponse>>

    suspend fun postAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun patchAnswer(
        answerId: Long,
        content: String,
    ): Flow<ApiResponse<Unit>>
}
