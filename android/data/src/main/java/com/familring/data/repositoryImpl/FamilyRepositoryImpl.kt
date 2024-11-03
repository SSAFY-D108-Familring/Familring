package com.familring.data.repositoryImpl

import com.familring.data.network.api.FamilyApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.User
import com.familring.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FamilyRepositoryImpl
    @Inject
    constructor(
        private val api: FamilyApi,
    ) : FamilyRepository {
        override suspend fun getFamilyMembers(): Flow<ApiResponse<List<User>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getFamilyMembers() },
                        default = emptyList(),
                    )
                emit(response)
            }
    }
