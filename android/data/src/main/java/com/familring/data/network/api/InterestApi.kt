package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface InterestApi {
    @GET("interests")
    suspend fun getInterests(): BaseResponse<Unit>

    @GET("interests/status")
    suspend fun getInterestStatus(): BaseResponse<Unit>

    @POST("interests/answers")
    suspend fun createAnswer(): BaseResponse<Unit>

    @GET("interests/answers/mine")
    suspend fun getMyAnswer(): BaseResponse<Unit>

    @PATCH("interests/answers/{interest-id}")
    suspend fun updateAnswer(): BaseResponse<Unit>

    @GET("interests/answers")
    suspend fun getAnswers(): BaseResponse<Unit>

    @GET("interests/answers/status")
    suspend fun getAnswerStatus(): BaseResponse<Unit>

    @GET("interests/answers/selected")
    suspend fun getSelectedAnswer(): BaseResponse<Unit>

    @POST("interests/mission/period")
    suspend fun setMissionPeriod(): BaseResponse<Unit>

    @GET("interests/mission/period")
    suspend fun getMissionPeriod(): BaseResponse<Unit>

    @POST("interests/mission")
    suspend fun uploadMission(): BaseResponse<Unit>

    @GET("interests/mission")
    suspend fun getMissions(): BaseResponse<Unit>

    @GET("interests/mission/{id}")
    suspend fun getMissionDetail(): BaseResponse<Unit>

    @PATCH("interests/mission/{id}")
    suspend fun updateMission(): BaseResponse<Unit>
}
