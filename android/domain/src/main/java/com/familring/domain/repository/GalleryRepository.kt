package com.familring.domain.repository

import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumResponse
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getAlbums(albumTypes: String): Flow<ApiResponse<AlbumResponse>>

    suspend fun createAlbum(
        scheduleId: Long?,
        albumName: String,
        albumType: String,
    ): Flow<ApiResponse<Unit>>
}
