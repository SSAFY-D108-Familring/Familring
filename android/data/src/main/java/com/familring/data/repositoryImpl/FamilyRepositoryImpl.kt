package com.familring.data.repositoryImpl

import com.familring.data.network.api.FamilyApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.User
import com.familring.domain.model.chat.Chat
import com.familring.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FamilyRepositoryImpl
    @Inject
    constructor(
        private val api: FamilyApi,
        private val authDataStore: AuthDataStore,
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

        override suspend fun getFamilyInfo(): Flow<ApiResponse<FamilyInfo>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getFamilyInfo() },
                        default = FamilyInfo(),
                    )
                if (response is ApiResponse.Success) {
                    authDataStore.saveFamilyId(response.data.familyId)
                }
                emit(response)
            }

        override suspend fun isAvailableCode(code: String): Flow<ApiResponse<Boolean>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.isAvailableCode(code) },
                        default = false,
                    )
                emit(response)
            }

        override suspend fun getParentAvailable(code: String): Flow<ApiResponse<List<String>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getParentAvailable(code) },
                        default = listOf(),
                    )
                emit(response)
            }

        override suspend fun enterRoom(
            roomId: Long,
            userId: Long,
        ): Flow<ApiResponse<List<Chat>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.enterRoom(
                                roomId = roomId,
                                userId = userId,
                            )
                        },
                        default = listOf(),
                    )
                emit(response)
            }

        override suspend fun joinFamily(familyCode: String): Flow<ApiResponse<String>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.joinFamily(familyCode) },
                        default = "",
                    )
                emit(response)
            }

        override suspend fun makeFamily(): Flow<ApiResponse<FamilyMake>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.makeFamily() },
                        default = FamilyMake(),
                    )
                emit(response)
            }

        override suspend fun getFamilyCode(): Flow<ApiResponse<String>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getFamilyCode() },
                        default = "",
                    )
                emit(response)
            }
    }
