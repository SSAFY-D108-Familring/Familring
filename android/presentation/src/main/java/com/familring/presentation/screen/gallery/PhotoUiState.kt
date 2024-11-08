package com.familring.presentation.screen.gallery

import androidx.compose.runtime.Stable
import com.familring.domain.model.gallery.Photo

@Stable
sealed interface PhotoUiState {
    object Loading : PhotoUiState

    data class Success(
        val photoList: List<Photo>,
    ) : PhotoUiState

    data class Error(
        val errorMessage: String,
    ) : PhotoUiState
}
