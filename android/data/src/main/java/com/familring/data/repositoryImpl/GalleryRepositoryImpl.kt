package com.familring.data.repositoryImpl

import com.familring.data.network.api.GalleryApi
import com.familring.data.network.response.emitApiResponse
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumResponse
import com.familring.domain.model.gallery.Photo
import com.familring.domain.repository.GalleryRepository
import com.familring.domain.request.CreateAlbumRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

        override suspend fun getOneAlbum(albumId: Long): Flow<ApiResponse<List<Photo>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getOneAlbum(albumId) },
                        default = emptyList(),
                    )
                emit(response)
            }
    }
