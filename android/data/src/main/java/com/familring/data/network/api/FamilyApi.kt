package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.User
import com.familring.domain.model.chat.Chat
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FamilyApi {
    @GET("family/code")
    suspend fun getFamilyCode(): BaseResponse<String>

    @POST("family")
    suspend fun makeFamily(): BaseResponse<FamilyMake>

    @POST("family/join")
    suspend fun joinFamily(
        @Body familyCode: String,
    ): BaseResponse<String>

    @GET("family/member")
    suspend fun getFamilyMembers(): BaseResponse<List<User>>

    @GET("family")
    suspend fun getFamilyInfo(): BaseResponse<FamilyInfo>

    @GET("family/code/{familyCode}")
    suspend fun isAvailableCode(
        @Path("familyCode") code: String,
    ): BaseResponse<Boolean>

    @GET("family/member/{familyCode}")
    suspend fun getParentAvailable(
        @Path("familyCode") code: String,
    ): BaseResponse<List<String>>

    @GET("family/rooms/enter/{roomId}")
    suspend fun enterRoom(
        @Path("roomId") roomId: Long,
        @Header("X-User-ID") userId: Long,
    ): BaseResponse<List<Chat>>

    @Multipart
    @POST("family/voice")
    suspend fun uploadVoice(
        @Part("fileUploadRequest") request: RequestBody,
        @Part voice: MultipartBody.Part?,
    ): BaseResponse<String>
}
