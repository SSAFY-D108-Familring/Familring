package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.gallery.Album
import com.familring.domain.model.gallery.AlbumResponse
import com.familring.domain.model.gallery.Photo
import com.familring.domain.request.CreateAlbumRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    ): BaseResponse<List<Photo>>
}
