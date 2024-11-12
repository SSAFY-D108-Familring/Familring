package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.interest.AnswerStatus
import com.familring.domain.model.interest.InterestCard
import com.familring.domain.model.interest.InterestPage
import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.InterestPeriodRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface InterestApi {
    @GET("interests/status")
    suspend fun getInterestStatus(): BaseResponse<Int>

    @GET("interests")
    suspend fun getInterests(
        @Query("pageNo") pageNo: Int,
    ): BaseResponse<InterestPage>

    @GET("interests/{interest-id}")
    suspend fun getInterestDetail(
        @Path("interest-id") interestId: Long,
    ): BaseResponse<List<Mission>>

    @POST("interests/answers")
    suspend fun createAnswer(
        @Body answer: AnswerRequest,
    ): BaseResponse<Unit>

    @PATCH("interests/answers")
    suspend fun updateAnswer(
        @Body answer: AnswerRequest,
    ): BaseResponse<Unit>

    @GET("interests/answers")
    suspend fun getAnswers(): BaseResponse<List<InterestCard>>

    @GET("interests/answers/status")
    suspend fun getAnswerStatus(): BaseResponse<AnswerStatus>

    @POST("interests/answers/selected")
    suspend fun selectInterest(): BaseResponse<Unit>

    @GET("interests/answers/selected")
    suspend fun getSelectedInterest(): BaseResponse<SelectedInterest>

    @POST("interests/missions/period")
    suspend fun setMissionPeriod(
        @Body endDate: InterestPeriodRequest,
    ): BaseResponse<Unit>

    @GET("interests/missions/period")
    suspend fun getMissionPeriod(): BaseResponse<Int>

    @Multipart
    @POST("interests/missions")
    suspend fun uploadMission(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @GET("interests/missions")
    suspend fun getMissions(): BaseResponse<List<Mission>>

    @GET("interest/missions/mine")
    suspend fun checkUploadMission(): BaseResponse<Boolean>
}
