package com.familring.presentation.screen.gallery

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface GalleryUiEvent {
    @Immutable
    data object Init : GalleryUiEvent

    @Immutable
    data object Loading : GalleryUiEvent

    @Immutable
    data object Success : GalleryUiEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : GalleryUiEvent
}
