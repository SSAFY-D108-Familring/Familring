package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.JwtToken
import com.familring.domain.model.User
import com.familring.domain.model.notification.KnockNotificationRequest
import com.familring.domain.model.notification.NotificationResponse
import com.familring.domain.request.UserEmotionRequest
import com.familring.domain.request.UserLoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("users/login")
    suspend fun login(
        @Body request: UserLoginRequest,
    ): BaseResponse<JwtToken>

    @Multipart
    @POST("users/join")
    suspend fun join(
        @Part("userJoinRequest") request: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<JwtToken>

    @GET("users")
    suspend fun getUser(): BaseResponse<User>

    @DELETE("users")
    suspend fun signOut(): BaseResponse<Unit>

    @POST("users/logout")
    suspend fun logOut(): BaseResponse<Unit>

    @PATCH("users/emotion")
    suspend fun updateEmotion(
        @Body userEmotion: UserEmotionRequest,
    ): BaseResponse<Unit>

    @PATCH("users/nickname")
    suspend fun updateNickname(
        @Query("userNickname") nickname: String,
    ): BaseResponse<Unit>

    @PATCH("users/color")
    suspend fun updateColor(
        @Query("userColor") color: String,
    ): BaseResponse<Unit>

    @Multipart
    @PATCH("users/face")
    suspend fun updateFace(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @PATCH("users/fcm-token")
    suspend fun updateFCMToken(
        @Query("fcmToken") token: String,
    ): BaseResponse<Unit>

    @POST("notifications/knock")
    suspend fun sendKnockNotification(
        @Body request: KnockNotificationRequest,
    ): BaseResponse<Unit>

    @GET("notifications")
    suspend fun getNotifications(): BaseResponse<List<NotificationResponse>>

    @PATCH("notifications/{notificationId}")
    suspend fun readNotification(
        @Path("notificationId") notificationId: Long,
    ): BaseResponse<Unit>
}
