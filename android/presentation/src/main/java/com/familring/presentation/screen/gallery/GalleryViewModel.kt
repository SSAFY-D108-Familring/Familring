package com.familring.presentation.screen.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumType
import com.familring.domain.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel
    @Inject
    constructor(
        val galleryRepository: GalleryRepository,
    ) : ViewModel() {
        private val _galleryUiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
        val galleryUiState = _galleryUiState.asStateFlow()

        private val _galleryUiEvent = MutableSharedFlow<GalleryUiEvent>()
        val galleryUiEvent = _galleryUiEvent.asSharedFlow()

        private val _photoUiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
        val photoUiState = _photoUiState.asStateFlow()

        private var currentAlbumTypes: List<AlbumType> =
            listOf(AlbumType.NORMAL, AlbumType.SCHEDULE, AlbumType.PERSON)

        fun getAlbums(albumTypes: List<AlbumType>) {
            Timber.d("된거임?")
            currentAlbumTypes = albumTypes
            loadAlbums()
        }

        private fun loadAlbums() {
            viewModelScope.launch {
                try {
                    val albumTypesString = currentAlbumTypes.joinToString(",") { it.name }
                    galleryRepository.getAlbums(albumTypesString).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _galleryUiState.value =
                                    GalleryUiState.Success(
                                        normalAlbums =
                                            (
                                                response.data.NORMAL?.toMutableList() ?: emptyList()
                                            ) + (
                                                response.data.SCHEDULE?.toMutableList() ?: emptyList()
                                            ),
                                        personAlbums = response.data.PERSON ?: emptyList(),
                                    )
                            }

                            is ApiResponse.Error -> {
                                _galleryUiState.value =
                                    GalleryUiState.Error(
                                        errorMessage = response.message,
                                    )
                            }
                        }
                    }
                } catch (e: Exception) {
                    _galleryUiState.value =
                        GalleryUiState.Error(
                            errorMessage = e.message ?: "Unknown error occurred",
                        )
                }
            }
        }

        fun createAlbum(
            scheduleId: Long?,
            albumName: String,
            albumType: AlbumType,
        ) {
            viewModelScope.launch {
                galleryRepository
                    .createAlbum(scheduleId, albumName, albumType.name)
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _galleryUiEvent.emit(GalleryUiEvent.Success)
                                loadAlbums()
                            }

                            is ApiResponse.Error -> {
                                _galleryUiEvent.emit(
                                    GalleryUiEvent.Error(
                                        response.code,
                                        response.message,
                                    ),
                                )
                                Timber.d("code: ${response.code}, message: ${response.message}")
                            }
                        }
                    }
            }
        }

        fun getOneAlbum(albumId: Long) {
            viewModelScope.launch {
                galleryRepository.getOneAlbum(albumId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _photoUiState.value =
                                PhotoUiState.Success(
                                    photoList = response.data,
                                )
                        }

                        is ApiResponse.Error -> {
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
