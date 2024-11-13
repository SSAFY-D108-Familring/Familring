package com.familring.data.repositoryImpl

import com.familring.data.network.api.QuestionApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.question.KnockRequest
import com.familring.domain.model.question.QuestionList
import com.familring.domain.model.question.QuestionResponse
import com.familring.domain.repository.QuestionRepository
import com.familring.domain.request.AnswerRequest
import com.familring.domain.request.QuestionPatchRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuestionRepositoryImpl
    @Inject
    constructor(
        private val api: QuestionApi,
    ) : QuestionRepository {
        override suspend fun getQuestion(questionId: Long?): Flow<ApiResponse<QuestionResponse>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getQuestion() },
                        default = QuestionResponse(),
                    )
                emit(response)
            }

        override suspend fun postAnswer(content: String): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.postAnswer(AnswerRequest(content)) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun patchAnswer(content: String): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.patchAnswer(QuestionPatchRequest(content)) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun getAllQuestion(
            pageNo: Int,
            order: String,
        ): Flow<ApiResponse<QuestionList>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAllQuestion(pageNo, order) },
                        default = QuestionList(),
                    )
                emit(response)
            }

        override suspend fun knockKnock(
            questionId: Long,
            receiverId: Long,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.knockKnock(KnockRequest(questionId, receiverId)) },
                        default = Unit,
                    )
                emit(response)
            }


    }
