package com.familring.data.repositoryImpl

import com.familring.data.network.api.CalendarApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.MonthSchedulesDailies
import com.familring.domain.model.Schedule
import com.familring.domain.repository.CalendarRepository
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
                // 날짜 형식 바꾸기!!!!
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

        override suspend fun createSchedule(schedule: Schedule): Flow<ApiResponse<Unit>> =
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
            schedule: Schedule,
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
