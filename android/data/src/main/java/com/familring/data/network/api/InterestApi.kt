package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.Profile
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
    ): BaseResponse<List<Profile>>

    @POST("interests/answers")
    suspend fun createAnswer(
        @Body answer: AnswerRequest,
    ): BaseResponse<Unit>

    @GET("interests/answers/mine")
    suspend fun getMyAnswer(): BaseResponse<String>

    @PATCH("interests/answers/{interest-id}")
    suspend fun updateAnswer(
        @Path("interest-id") interestId: Long,
    ): BaseResponse<Unit>

    @GET("interests/answers")
    suspend fun getAnswers(): BaseResponse<List<InterestCard>>

    @GET("interests/answers/status")
    suspend fun getAnswerStatus(): BaseResponse<Boolean>

    @GET("interests/answers/selected")
    suspend fun getSelectedAnswer(): BaseResponse<SelectedInterest>

    @POST("interests/mission/period")
    suspend fun setMissionPeriod(
        @Body endDate: InterestPeriodRequest,
    ): BaseResponse<Unit>

    @GET("interests/mission/period")
    suspend fun getMissionPeriod(): BaseResponse<Int>

    @Multipart
    @POST("interests/mission")
    suspend fun uploadMission(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @GET("interests/mission")
    suspend fun getMissions(): BaseResponse<List<Mission>>
}
