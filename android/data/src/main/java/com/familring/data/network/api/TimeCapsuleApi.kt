package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.TimeCapsule
import com.familring.domain.model.TimeCapsuleStatus
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeCapsuleApi {
    @GET("timecapsules/status")
    suspend fun getTimeCapsuleStatus(): BaseResponse<TimeCapsuleStatus>

    @POST("timecapsules")
    suspend fun createTimeCapsule(
        @Body date: String,
    ): BaseResponse<Unit>

    @POST("timecapsules/answers")
    suspend fun createTimeCapsuleAnswer(
        @Body content: String,
    ): BaseResponse<Unit>

    @GET("timecapsules")
    suspend fun getTimeCapsules(): BaseResponse<List<TimeCapsule>>
}
