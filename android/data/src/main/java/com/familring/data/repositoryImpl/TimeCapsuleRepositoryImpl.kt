package com.familring.data.repositoryImpl

import com.familring.data.network.api.TimeCapsuleApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.timecapsule.TimeCapsulePage
import com.familring.domain.model.timecapsule.TimeCapsuleStatus
import com.familring.domain.repository.TimeCapsuleRepository
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.CreateTimeCapsuleRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class TimeCapsuleRepositoryImpl
    @Inject
    constructor(
        private val timeCapsuleApi: TimeCapsuleApi,
    ) : TimeCapsuleRepository {
        override suspend fun getTimeCapsuleStatus(): Flow<ApiResponse<TimeCapsuleStatus>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsuleStatus() },
                        default = TimeCapsuleStatus(),
                    )
                emit(apiResponse)
            }

        override suspend fun createTimeCapsule(openDate: LocalDate): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = {
                            timeCapsuleApi.createTimeCapsule(
                                date =
                                    CreateTimeCapsuleRequest(
                                        date = openDate,
                                    ),
                            )
                        },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun createTimeCapsuleAnswer(content: String): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = {
                            timeCapsuleApi.createTimeCapsuleAnswer(
                                content = AnswerRequest(content),
                            )
                        },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getTimeCapsules(pageNo: Int): Flow<ApiResponse<TimeCapsulePage>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsules(pageNo) },
                        default = TimeCapsulePage(),
                    )
                emit(apiResponse)
            }
    }
