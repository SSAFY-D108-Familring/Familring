package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.FaceCount
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FaceApi {
    @Multipart
    @POST("face-recognition/face-count")
    suspend fun getFaceCount(
        @Part file: MultipartBody.Part?,
    ): BaseResponse<FaceCount>
}
