package com.familring.data.repositoryImpl

import com.familring.data.network.api.TimeCapsuleApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.TimeCapsule
import com.familring.domain.repository.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TimeCapsuleRepositoryImpl
    @Inject
    constructor(
        private val timeCapsuleApi: TimeCapsuleApi,
    ) : TimeCapsuleRepository {
        override suspend fun getTimeCapsuleStatus(): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsuleStatus() },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun createTimeCapsule(timeCapsule: TimeCapsule): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.createTimeCapsule(timeCapsule) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun createTimeCapsuleAnswer(): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.createTimeCapsuleAnswer() },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getTimeCapsuleWriters(): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsuleWriters() },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getTimeCapsules(): Flow<ApiResponse<List<TimeCapsule>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsules() },
                        default = listOf<TimeCapsule>(),
                    )
            }

        override suspend fun getTimeCapsuleAnswers(): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { timeCapsuleApi.getTimeCapsuleAnswers() },
                        default = Unit,
                    )
                emit(apiResponse)
            }
    }
