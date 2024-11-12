package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.User
import com.familring.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow

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
    ): Flow<ApiResponse<List<Chat>>>
}
