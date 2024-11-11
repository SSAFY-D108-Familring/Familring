package com.familring.domain.repository

import android.net.Uri
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumResponse
import com.familring.domain.model.gallery.OneAlbumResponse
import com.familring.domain.model.gallery.Photo
import kotlinx.coroutines.flow.Flow
import java.io.File

interface GalleryRepository {
    suspend fun getAlbums(albumTypes: String): Flow<ApiResponse<AlbumResponse>>

    suspend fun createAlbum(
        scheduleId: Long?,
        albumName: String,
        albumType: String,
    ): Flow<ApiResponse<Unit>>

    suspend fun getOneAlbum(albumId: Long): Flow<ApiResponse<OneAlbumResponse>>

    suspend fun updateAlbum(
        albumId: Long,
        albumName: String,
    ): Flow<ApiResponse<Unit>>

    suspend fun deleteAlbum(albumId: Long): Flow<ApiResponse<Unit>>

    suspend fun uploadPhotos(
        albumId: Long,
        photos: List<File>,
    ): Flow<ApiResponse<Unit>>

    suspend fun deletePhotos(
        albumId: Long,
        photoIds: List<Long>,
    ): Flow<ApiResponse<Unit>>
}
