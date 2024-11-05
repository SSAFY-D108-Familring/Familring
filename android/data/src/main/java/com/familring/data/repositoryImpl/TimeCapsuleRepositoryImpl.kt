package com.familring.data.repositoryImpl

import com.familring.data.network.api.TimeCapsuleApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.timecapsule.TimeCapsule
import com.familring.domain.model.timecapsule.TimeCapsuleStatus
import com.familring.domain.repository.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

        override suspend fun createTimeCapsule(openDate: String): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.createTimeCapsule(openDate) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun createTimeCapsuleAnswer(content: String): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.createTimeCapsuleAnswer(content) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getTimeCapsules(): Flow<ApiResponse<List<TimeCapsule>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsules() },
                        default = listOf(),
                    )
                emit(apiResponse)
            }
    }
