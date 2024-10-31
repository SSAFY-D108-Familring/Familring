package com.familring.data.repositoryImpl

import com.familring.data.network.api.UserApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.datasource.AuthDataSource
import com.familring.domain.datasource.TokenDataSource
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserLoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val api: UserApi,
        private val tokenDataSource: TokenDataSource,
        private val authDataSource: AuthDataSource,
    ) : UserRepository {
        override suspend fun login(request: UserLoginRequest): Flow<ApiResponse<JwtToken>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.login(request) },
                        default = JwtToken(),
                    )
                if (response is ApiResponse.Success) {
                    tokenDataSource.saveJwtToken(
                        jwtToken = response.data,
                    )
                    authDataSource.saveKakaoId(request.userKakaoId)
                }
                emit(response)
            }
    }
