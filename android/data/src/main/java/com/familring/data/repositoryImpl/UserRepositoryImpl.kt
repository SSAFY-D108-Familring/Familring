package com.familring.data.repositoryImpl

import com.familring.data.network.api.UserApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.datastore.TokenDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.model.User
import com.familring.domain.model.notification.KnockNotificationRequest
import com.familring.domain.model.notification.NotificationResponse
import com.familring.domain.model.notification.SendMentionNotificationRequest
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserEmotionRequest
import com.familring.domain.request.UserJoinRequest
import com.familring.domain.request.UserLoginRequest
import com.familring.domain.util.toMultiPart
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
        private val tokenDataStore: TokenDataStore,
        private val authDataSource: AuthDataStore,
        private val gson: Gson,
    ) : UserRepository {
        override suspend fun login(request: UserLoginRequest): Flow<ApiResponse<JwtToken>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.login(request) },
                        default = JwtToken(),
                    )
                if (response is ApiResponse.Success) {
                    tokenDataStore.saveJwtToken(
                        jwtToken =
                            JwtToken(
                                grantType = response.data.grantType,
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken,
                            ),
                    )
                    authDataSource.saveUserId(
                        userId = response.data.userId,
                    )
                }
                emit(response)
            }

        override suspend fun join(
            request: UserJoinRequest,
            userFace: File,
        ): Flow<ApiResponse<JwtToken>> =
            flow {
                val image = userFace.toMultiPart(filename = "image")
                val requestBody =
                    gson.toJson(request).toRequestBody("application/json".toMediaTypeOrNull())
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.join(
                                request = requestBody,
                                image = image,
                            )
                        },
                        default = JwtToken(),
                    )
                if (response is ApiResponse.Success) {
                    tokenDataStore.saveJwtToken(
                        jwtToken =
                            JwtToken(
                                grantType = response.data.grantType,
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken,
                            ),
                    )
                    authDataSource.saveUserId(
                        userId = response.data.userId,
                    )
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
                    authDataSource.saveUserId(response.data.userId)
                }
                emit(response)
            }

        override suspend fun signOut(): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.signOut() },
                        default = Unit,
                    )
                if (response is ApiResponse.Success) {
                    tokenDataStore.deleteJwtToken()
                    authDataSource.deleteAuthData()
                }
                emit(response)
            }

        override suspend fun logOut(): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.logOut() },
                        default = Unit,
                    )
                if (response is ApiResponse.Success) {
                    tokenDataStore.deleteJwtToken()
                    authDataSource.deleteAuthData()
                }
                emit(response)
            }

        override suspend fun updateEmotion(emotion: UserEmotionRequest): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateEmotion(emotion) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun updateNickname(nickname: String): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateNickname(nickname) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun updateColor(color: String): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateColor(color) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun sendKnockNotification(
            targetUserId: String,
            senderNickname: String,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val request =
                    KnockNotificationRequest(
                        targetUserId = targetUserId,
                        senderNickname = senderNickname,
                    )
                val response =
                    emitApiResponse(
                        apiResponse = { api.sendKnockNotification(request) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun getNotifications(): Flow<ApiResponse<List<NotificationResponse>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getNotifications() },
                        default = listOf(),
                    )
                emit(response)
            }

        override suspend fun readNotification(notificationId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.readNotification(notificationId) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun updateFCMToken(token: String): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateFcmToken(token) },
                        default = Unit,
                    )
                if (response is ApiResponse.Success) {
                    authDataSource.saveFCMToken(token)
                }
                emit(response)
            }

        override suspend fun updateFace(face: File): Flow<ApiResponse<Unit>> =
            flow {
                val image = face.toMultiPart(filename = "image")
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateFace(image) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun sendMentionNotification(
            receiverId: Long,
            mention: String,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.sendMentionNotification(
                                SendMentionNotificationRequest(
                                    receiverId,
                                    mention,
                                ),
                            )
                        },
                        default = Unit,
                    )
                emit(response)
            }
    }
