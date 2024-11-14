package com.familring.data.repositoryImpl

import com.familring.data.network.api.DailyApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.repository.DailyRepository
import com.familring.domain.util.toRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class DailyRepositoryImpl
    @Inject
    constructor(
        private val dailyApi: DailyApi,
    ) : DailyRepository {
        override suspend fun getDayDailies(
            year: Int,
            month: Int,
            day: Int,
        ): Flow<ApiResponse<List<DailyLife>>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { dailyApi.getDayDailies(year, month, day) },
                        default = listOf(),
                    )
                emit(apiResponse)
            }

        override suspend fun createDaily(
            content: String,
            image: MultipartBody.Part?,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { dailyApi.createDaily(content.toRequestBody(), image) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun modifyDaily(
            dailyId: Long,
            content: String,
            image: MultipartBody.Part?,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { dailyApi.modifyDaily(dailyId, content.toRequestBody(), image) },
                        default = Unit,
                    )
                emit(apiResponse)
            }

        override suspend fun deleteDaily(dailyId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val apiResponse =
                    emitApiResponse(
                        apiResponse = { dailyApi.deleteDaily(dailyId) },
                        default = Unit,
                    )
                emit(apiResponse)
            }
    }
