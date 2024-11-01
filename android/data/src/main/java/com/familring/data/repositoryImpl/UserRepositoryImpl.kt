package com.familring.data.repositoryImpl

import com.familring.data.network.api.UserApi
import com.familring.data.network.response.emitApiResponse
import com.familring.data.util.toMultiPart
import com.familring.domain.datasource.AuthDataSource
import com.familring.domain.datasource.TokenDataSource
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.model.User
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserJoinRequest
import com.familring.domain.request.UserLoginRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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

        override suspend fun join(
            request: UserJoinRequest,
            userFace: File,
        ): Flow<ApiResponse<JwtToken>> =
            flow {
                val image = userFace.toMultiPart()
                val requestBody =
                    Gson().toJson(request).toRequestBody("application/json".toMediaTypeOrNull())
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.join(
                                request = requestBody,
                                userFace = image!!,
                            )
                        },
                        default = JwtToken(),
                    )
                if (response is ApiResponse.Success) {
                    tokenDataSource.saveJwtToken(response.data)
                    authDataSource.saveKakaoId(request.userKakaoId)
                }
                emit(response)
            }

        override suspend fun getUser(): Flow<ApiResponse<User>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getUser() },
                        default = User(),
                    )
                if (response is ApiResponse.Success) {
                    authDataSource.saveNickname(response.data.userNickname)
                    authDataSource.saveBirthDate(response.data.userBirthDate)
                    authDataSource.saveZodiacSign(response.data.userZodiacSign)
                    authDataSource.saveRole(response.data.userRole)
                    authDataSource.saveFace(response.data.userFace)
                    authDataSource.saveColor(response.data.userColor)
                    authDataSource.saveEmotion(response.data.userEmotion)
                }
                emit(response)
            }
    }
