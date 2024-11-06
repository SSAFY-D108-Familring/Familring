package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.QuestionResponse
import com.familring.domain.request.QuestionAnswerRequest
import com.familring.domain.request.QuestionPatchRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionApi {
    @GET("questions")
    suspend fun getQuestion(): BaseResponse<QuestionResponse>

    @POST("questions/answers")
    suspend fun postAnswer(
        @Body request: QuestionAnswerRequest,
    ): BaseResponse<Unit>

    @PATCH("questions/answers/{answer_id}")
    suspend fun patchAnswer(
        @Path("answer_id") answerId: Long,
        @Body request: QuestionPatchRequest,
    ): BaseResponse<Unit>
}
