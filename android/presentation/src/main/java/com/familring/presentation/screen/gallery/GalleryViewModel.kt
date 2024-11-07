package com.familring.presentation.screen.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumType
import com.familring.domain.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel
    @Inject
    constructor(
        val galleryRepository: GalleryRepository,
    ) : ViewModel() {
        private val _galleryUiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
        val galleryUiState = _galleryUiState.asStateFlow()

        fun getAlbums(albumTypes: List<AlbumType>) {
            viewModelScope.launch {
                try {
                    // AlbumType을 문자열로 변환하여 쉼표로 구분된 하나의 문자열로 전달
                    val albumTypesString = albumTypes.joinToString(",") { it.name }
                    galleryRepository.getAlbums(albumTypesString).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _galleryUiState.value =
                                    GalleryUiState.Success(
                                        normalAlbums = response.data.NORMAL ?: emptyList(),
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
    }
