package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.User
import com.familring.domain.model.chat.ChatResponse
import com.familring.domain.model.chat.FileUploadRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FamilyRepository {
    suspend fun getFamilyCode(): Flow<ApiResponse<String>>

    suspend fun makeFamily(): Flow<ApiResponse<FamilyMake>>

    suspend fun getFamilyMembers(): Flow<ApiResponse<List<User>>>

    suspend fun joinFamily(familyCode: String): Flow<ApiResponse<String>>

    suspend fun getFamilyInfo(): Flow<ApiResponse<FamilyInfo>>

    suspend fun isAvailableCode(code: String): Flow<ApiResponse<Boolean>>

    suspend fun getParentAvailable(code: String): Flow<ApiResponse<List<String>>>

    suspend fun enterRoom(
        roomId: Long,
        userId: Long,
        page: Int,
        size: Int,
    ): Flow<ApiResponse<ChatResponse>>

    suspend fun uploadVoice(
        request: FileUploadRequest,
        voice: File,
    ): Flow<ApiResponse<String>>

    suspend fun uploadImage(
        request: FileUploadRequest,
        photo: File,
    ): Flow<ApiResponse<String>>
}
