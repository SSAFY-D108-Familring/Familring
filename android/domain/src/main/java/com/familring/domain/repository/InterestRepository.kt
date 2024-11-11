package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.interest.AnswerStatus
import com.familring.domain.model.interest.InterestCard
import com.familring.domain.model.interest.InterestPage
import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.request.InterestPeriodRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface InterestRepository {
    suspend fun getInterestStatus(): Flow<ApiResponse<Int>>

    suspend fun getInterests(pageNo: Int): Flow<ApiResponse<InterestPage>>

    suspend fun getInterestDetail(interestId: Long): Flow<ApiResponse<List<Mission>>>

    suspend fun createAnswer(answer: String): Flow<ApiResponse<Unit>>

    suspend fun updateAnswer(content: String): Flow<ApiResponse<Unit>>

    suspend fun getAnswers(): Flow<ApiResponse<List<InterestCard>>>

    suspend fun getAnswerStatus(): Flow<ApiResponse<AnswerStatus>>

    suspend fun getSelectedAnswer(): Flow<ApiResponse<SelectedInterest>>

    suspend fun setMissionPeriod(endDate: InterestPeriodRequest): Flow<ApiResponse<Unit>>

    suspend fun getMissionPeriod(): Flow<ApiResponse<Int>>

    suspend fun uploadMission(image: MultipartBody.Part?): Flow<ApiResponse<Unit>>

    suspend fun getMissions(): Flow<ApiResponse<List<Mission>>>
}
