package com.familring.data.repositoryImpl

import com.familring.data.network.api.GalleryApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumName
import com.familring.domain.model.gallery.AlbumResponse
import com.familring.domain.model.gallery.OneAlbumResponse
import com.familring.domain.repository.GalleryRepository
import com.familring.domain.request.CreateAlbumRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class GalleryRepositoryImpl
    @Inject
    constructor(
        val api: GalleryApi,
    ) : GalleryRepository {
        override suspend fun getAlbums(albumTypes: String): Flow<ApiResponse<AlbumResponse>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAlbums(albumTypes) },
                        default = AlbumResponse(),
                    )
                emit(response)
            }

        override suspend fun createAlbum(
            scheduleId: Long?,
            albumName: String,
            albumType: String,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.createAlbum(
                                CreateAlbumRequest(
                                    scheduleId,
                                    albumName,
                                    albumType,
                                ),
                            )
                        },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun getOneAlbum(albumId: Long): Flow<ApiResponse<OneAlbumResponse>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getOneAlbum(albumId) },
                        default = OneAlbumResponse(),
                    )
                emit(response)
            }

        override suspend fun updateAlbum(
            albumId: Long,
            albumName: String,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.updateAlbum(albumId, AlbumName(albumName)) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun deleteAlbum(albumId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.deleteAlbum(albumId) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun uploadPhotos(
            albumId: Long,
            photos: List<File>,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val parts =
                    photos.map { file ->
                        val extension = file.extension.lowercase()
                        val mediaType =
                            when (extension) {
                                "jpg", "jpeg" -> "image/jpeg"
                                "png" -> "image/png"
                                else -> "image/*"
                            }.toMediaTypeOrNull()

                        MultipartBody.Part.createFormData(
                            name = "photos",
                            filename = file.name,
                            body = file.asRequestBody(mediaType),
                        )
                    }

                emit(
                    emitApiResponse(
                        apiResponse = { api.uploadPhotos(albumId, parts) },
                        default = Unit,
                    ),
                )
            }
    }
