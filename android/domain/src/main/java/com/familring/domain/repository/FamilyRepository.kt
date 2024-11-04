package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyMake
import kotlinx.coroutines.flow.Flow

interface FamilyRepository {
    suspend fun getFamilyCode(): Flow<ApiResponse<String>>

    suspend fun makeFamily(): Flow<ApiResponse<FamilyMake>>

    suspend fun joinFamily(familyCode: String): Flow<ApiResponse<String>>
}
