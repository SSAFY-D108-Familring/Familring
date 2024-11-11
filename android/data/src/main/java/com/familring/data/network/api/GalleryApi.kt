package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.gallery.AlbumName
import com.familring.domain.model.gallery.AlbumResponse
import com.familring.domain.model.gallery.OneAlbumResponse
import com.familring.domain.request.CreateAlbumRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GalleryApi {
    @GET("albums")
    suspend fun getAlbums(
        @Query("album_type") albumTypes: String,
    ): BaseResponse<AlbumResponse>

    @POST("albums")
    suspend fun createAlbum(
        @Body request: CreateAlbumRequest,
    ): BaseResponse<Unit>

    @GET("albums/{album_id}")
    suspend fun getOneAlbum(
        @Path("album_id") albumId: Long,
    ): BaseResponse<OneAlbumResponse>

    @PATCH("albums/{album_id}")
    suspend fun updateAlbum(
        @Path("album_id") albumId: Long,
        @Body request: AlbumName,
    ): BaseResponse<Unit>

    @DELETE("albums/{album_id}")
    suspend fun deleteAlbum(
        @Path("album_id") albumId: Long,
    ): BaseResponse<Unit>

    @Multipart
    @POST("albums/{album_id}/photos")
    suspend fun uploadPhotos(
        @Path("album_id") albumId: Long,
        @Part photos: List<MultipartBody.Part?>,
    ): BaseResponse<Unit>

    @HTTP(method = "DELETE", path = "albums/{album_id}/photos", hasBody = true)
    suspend fun deletePhotos(
        @Path("album_id") albumId: Long,
        @Body photoIds: List<Long>,
    ): BaseResponse<Unit>
}
