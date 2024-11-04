package com.familring.data.repositoryImpl

import com.familring.data.network.api.FamilyApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyMake
import com.familring.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FamilyRepositoryImpl
    @Inject
    constructor(
        private val familyApi: FamilyApi,
    ) : FamilyRepository {
        override suspend fun getFamilyCode(): Flow<ApiResponse<String>> {
            TODO("Not yet implemented")
        }

        override suspend fun makeFamily(): Flow<ApiResponse<FamilyMake>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { familyApi.makeFamily() },
                        default = FamilyMake(),
                    )
                emit(response)
            }

        override suspend fun joinFamily(familyCode: String): Flow<ApiResponse<String>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { familyApi.joinFamily(familyCode) },
                        default = "",
                    )
                emit(response)
            }
    }
