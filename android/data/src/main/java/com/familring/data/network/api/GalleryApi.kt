package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.gallery.AlbumResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryApi {
    @GET("albums")
    suspend fun getAlbums(
        @Query("album_type") albumTypes: String,
    ): BaseResponse<AlbumResponse>
}
