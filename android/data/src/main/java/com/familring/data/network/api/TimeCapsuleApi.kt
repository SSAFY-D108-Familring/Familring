package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.timecapsule.TimeCapsulePage
import com.familring.domain.model.timecapsule.TimeCapsuleStatus
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.CreateTimeCapsuleRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimeCapsuleApi {
    @GET("timecapsules/status")
    suspend fun getTimeCapsuleStatus(): BaseResponse<TimeCapsuleStatus>

    @POST("timecapsules")
    suspend fun createTimeCapsule(
        @Body date: CreateTimeCapsuleRequest,
    ): BaseResponse<Unit>

    @POST("timecapsules/answers")
    suspend fun createTimeCapsuleAnswer(
        @Body content: AnswerRequest,
    ): BaseResponse<Unit>

    @GET("timecapsules")
    suspend fun getTimeCapsules(
        @Query("pageNo") pageNo: Int,
    ): BaseResponse<TimeCapsulePage>
}
