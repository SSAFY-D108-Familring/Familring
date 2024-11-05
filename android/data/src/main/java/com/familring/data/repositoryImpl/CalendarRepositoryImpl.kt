package com.familring.data.repositoryImpl

import com.familring.data.network.api.CalendarApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.MonthSchedulesDailies
import com.familring.domain.model.calendar.Schedule
import com.familring.domain.repository.CalendarRepository
import com.familring.domain.request.ScheduleCreateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalendarRepositoryImpl
    @Inject
    constructor(
        private val calendarApi: CalendarApi,
    ) : CalendarRepository {
        override suspend fun getMonthData(
            year: Int,
            month: Int,
        ): Flow<ApiResponse<MonthSchedulesDailies>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { calendarApi.getMonthData(year, month) },
                        default = MonthSchedulesDailies(),
                    )
                emit(apiResponse)
            }

        override suspend fun getDaySchedules(scheduleIds: List<Long>): Flow<ApiResponse<List<Schedule>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { calendarApi.getDaySchedules(scheduleIds) },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun createSchedule(schedule: ScheduleCreateRequest): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { calendarApi.createSchedule(schedule) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun updateSchedule(
            id: Long,
            schedule: ScheduleCreateRequest,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { calendarApi.updateSchedule(id, schedule) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun deleteSchedule(id: Long): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { calendarApi.deleteSchedule(id) },
                        default = Unit,
                    )
                emit(apiResponse)
            }
    }
