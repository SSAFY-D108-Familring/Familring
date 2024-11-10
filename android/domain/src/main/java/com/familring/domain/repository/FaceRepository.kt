package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FaceRepository {
    suspend fun getFaceCount(face: File): Flow<ApiResponse<Int>>
}
