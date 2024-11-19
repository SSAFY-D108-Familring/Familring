package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.MonthSchedulesDailies
import com.familring.domain.model.calendar.Schedule
import com.familring.domain.request.ScheduleCreateRequest
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun getMonthData(
        year: Int,
        month: Int,
    ): Flow<ApiResponse<MonthSchedulesDailies>>

    suspend fun getDaySchedules(
        year: Int,
        month: Int,
        day: Int,
    ): Flow<ApiResponse<List<Schedule>>>

    suspend fun createSchedule(schedule: ScheduleCreateRequest): Flow<ApiResponse<Unit>>

    suspend fun updateSchedule(
        id: Long,
        schedule: ScheduleCreateRequest,
    ): Flow<ApiResponse<Unit>>

    suspend fun deleteSchedule(id: Long): Flow<ApiResponse<Unit>>
}
