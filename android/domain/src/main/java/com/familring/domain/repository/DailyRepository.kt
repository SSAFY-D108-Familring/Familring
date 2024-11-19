package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.DailyLife
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface DailyRepository {
    suspend fun getDayDailies(
        year: Int,
        month: Int,
        day: Int,
    ): Flow<ApiResponse<List<DailyLife>>>

    suspend fun createDaily(
        content: String,
        image: MultipartBody.Part?,
    ): Flow<ApiResponse<Unit>>

    suspend fun modifyDaily(
        dailyId: Long,
        content: String,
        image: MultipartBody.Part?,
    ): Flow<ApiResponse<Unit>>

    suspend fun deleteDaily(dailyId: Long): Flow<ApiResponse<Unit>>
}
