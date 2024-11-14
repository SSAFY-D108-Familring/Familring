package com.familring.data.repositoryImpl

import com.familring.data.network.api.ScheduleApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.MonthSchedulesDailies
import com.familring.domain.model.calendar.Schedule
import com.familring.domain.repository.ScheduleRepository
import com.familring.domain.request.ScheduleCreateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScheduleRepositoryImpl
    @Inject
    constructor(
        private val scheduleApi: ScheduleApi,
    ) : ScheduleRepository {
        override suspend fun getMonthData(
            year: Int,
            month: Int,
        ): Flow<ApiResponse<MonthSchedulesDailies>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { scheduleApi.getMonthData(year, month) },
                        default = MonthSchedulesDailies(),
                    )
                emit(apiResponse)
            }

        override suspend fun getDaySchedules(
            year: Int,
            month: Int,
            day: Int,
        ): Flow<ApiResponse<List<Schedule>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { scheduleApi.getDaySchedules(year, month, day) },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun createSchedule(schedule: ScheduleCreateRequest): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { scheduleApi.createSchedule(schedule) },
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
                        apiResponse = { scheduleApi.updateSchedule(id, schedule) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun deleteSchedule(id: Long): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { scheduleApi.deleteSchedule(id) },
                        default = Unit,
                    )
                emit(apiResponse)
            }
    }
