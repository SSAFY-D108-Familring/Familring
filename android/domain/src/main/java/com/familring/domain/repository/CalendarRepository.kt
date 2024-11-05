package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.MonthSchedulesDailies
import com.familring.domain.model.calendar.Schedule
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    suspend fun getMonthData(
        year: Int,
        month: Int,
    ): Flow<ApiResponse<MonthSchedulesDailies>>

    suspend fun getDaySchedules(scheduleIds: List<Long>): Flow<ApiResponse<List<Schedule>>>

    suspend fun createSchedule(schedule: Schedule): Flow<ApiResponse<Unit>>

    suspend fun updateSchedule(
        id: Long,
        schedule: Schedule,
    ): Flow<ApiResponse<Unit>>

    suspend fun deleteSchedule(id: Long): Flow<ApiResponse<Unit>>
}
