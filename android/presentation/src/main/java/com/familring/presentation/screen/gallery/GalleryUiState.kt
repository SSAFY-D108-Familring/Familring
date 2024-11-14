package com.familring.presentation.screen.gallery

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.gallery.Album

@Stable
sealed interface GalleryUiState {
    @Immutable
    data object Loading : GalleryUiState

    @Immutable
    data class Success(
        val normalAlbums: List<Album> = emptyList(),
        val personAlbums: List<Album> = emptyList(),
        val scheduleAlbums: List<Album> = emptyList(),
    ) : GalleryUiState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : GalleryUiState
}
