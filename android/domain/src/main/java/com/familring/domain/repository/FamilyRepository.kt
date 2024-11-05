package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FamilyRepository {
    suspend fun getFamilyCode(): Flow<ApiResponse<String>>

    suspend fun makeFamily(): Flow<ApiResponse<FamilyMake>>
    suspend fun getFamilyMembers(): Flow<ApiResponse<List<User>>>

    suspend fun joinFamily(familyCode: String): Flow<ApiResponse<String>>
    suspend fun getFamilyInfo(): Flow<ApiResponse<FamilyInfo>>
}
