package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.question.QuestionList
import com.familring.domain.model.question.QuestionResponse
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun getQuestion(questionId: Long? = null): Flow<ApiResponse<QuestionResponse>>

    suspend fun postAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun patchAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun getAllQuestion(
        pageNo: Int,
        order: String,
    ): Flow<ApiResponse<QuestionList>>
}
