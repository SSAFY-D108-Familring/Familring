package com.familring.data.repositoryImpl

import com.familring.data.network.api.FaceApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.FaceRepository
import com.familring.domain.util.toMultiPart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class FaceRepositoryImpl
    @Inject
    constructor(
        private val api: FaceApi,
    ) : FaceRepository {
        override suspend fun getFaceCount(face: File): Flow<ApiResponse<Int>> =
            flow {
                val image = face.toMultiPart(filename = "file")
                val response =
                    emitApiResponse(
                        apiResponse = { api.getFaceCount(image) },
                        default = 0,
                    )
                emit(response)
            }
    }
