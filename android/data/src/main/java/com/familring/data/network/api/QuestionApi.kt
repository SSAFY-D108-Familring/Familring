package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.question.QuestionList
import com.familring.domain.model.question.QuestionResponse
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.QuestionPatchRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface QuestionApi {
    @GET("questions")
    suspend fun getQuestion(
        @Query("questionId") questionId: Long? = null,
    ): BaseResponse<QuestionResponse>

    @POST("questions/answers")
    suspend fun postAnswer(
        @Body request: AnswerRequest,
    ): BaseResponse<Unit>

    @PATCH("questions/answers")
    suspend fun patchAnswer(
        @Body request: QuestionPatchRequest,
    ): BaseResponse<Unit>

    @GET("questions/all")
    suspend fun getAllQuestion(
        @Query("pageNo") pageNo: Int,
        @Query("order") order: String,
    ): BaseResponse<QuestionList>
}
