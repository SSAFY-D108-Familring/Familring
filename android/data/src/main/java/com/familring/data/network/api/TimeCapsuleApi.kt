package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.TimeCapsule
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeCapsuleApi {
    @GET("timecapsules/status")
    suspend fun getTimeCapsuleStatus(): BaseResponse<Unit>

    @POST("timecapsules")
    suspend fun createTimeCapsule(timeCapsule: TimeCapsule): BaseResponse<Unit>

    @POST("timecapsules/answers")
    suspend fun createTimeCapsuleAnswer(): BaseResponse<Unit>

    @GET("timecapsules/authors")
    suspend fun getTimeCapsuleWriters(): BaseResponse<Unit>

    @GET("timecapsules")
    suspend fun getTimeCapsules(): BaseResponse<List<TimeCapsule>>

    @GET("timecapsules/answers")
    suspend fun getTimeCapsuleAnswers(): BaseResponse<Unit>
}
