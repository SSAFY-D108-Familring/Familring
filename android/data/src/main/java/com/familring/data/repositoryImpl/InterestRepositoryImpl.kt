package com.familring.data.repositoryImpl

import com.familring.data.network.api.InterestApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.interest.AnswerStatus
import com.familring.domain.model.interest.InterestCard
import com.familring.domain.model.interest.InterestPage
import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.repository.InterestRepository
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.InterestPeriodRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class InterestRepositoryImpl
    @Inject
    constructor(
        private val interestApi: InterestApi,
    ) : InterestRepository {
        override suspend fun getInterestStatus(): Flow<ApiResponse<Int>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getInterestStatus() },
                        default = 0,
                    )
                emit(apiResponse)
            }

        override suspend fun getInterests(pageNo: Int): Flow<ApiResponse<InterestPage>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getInterests(pageNo) },
                        default = InterestPage(),
                    )
                emit(apiResponse)
            }

        override suspend fun getInterestDetail(interestId: Long): Flow<ApiResponse<List<Mission>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getInterestDetail(interestId) },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun createAnswer(answer: String): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.createAnswer(AnswerRequest(answer)) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun updateAnswer(content: String): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.updateAnswer(AnswerRequest(content)) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getAnswers(): Flow<ApiResponse<List<InterestCard>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getAnswers() },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun getAnswerStatus(): Flow<ApiResponse<AnswerStatus>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getAnswerStatus() },
                        default = AnswerStatus(),
                    )
                emit(apiResponse)
            }

        override suspend fun selectInterest(): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.selectInterest() },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getSelectedInterest(): Flow<ApiResponse<SelectedInterest>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getSelectedInterest() },
                        default = SelectedInterest(),
                    )
                emit(apiResponse)
            }

        override suspend fun setMissionPeriod(endDate: InterestPeriodRequest): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.setMissionPeriod(endDate) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getMissionPeriod(): Flow<ApiResponse<Int>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getMissionPeriod() },
                        default = 0,
                    )
                emit(apiResponse)
            }

        override suspend fun uploadMission(image: MultipartBody.Part?): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.uploadMission(image) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun getMissions(): Flow<ApiResponse<List<Mission>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.getMissions() },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun checkUploadMission(): Flow<ApiResponse<Boolean>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { interestApi.checkUploadMission() },
                        default = false,
                    )
                emit(apiResponse)
            }
    }
