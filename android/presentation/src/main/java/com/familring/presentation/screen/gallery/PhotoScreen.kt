package com.familring.presentation.screen.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Photo
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.White

@Composable
fun PhotoRoute(
    albumId: Long,
    photoUrl: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: GalleryViewModel = hiltViewModel(),
) {
    val photoUiState by viewModel.photoUiState.collectAsStateWithLifecycle()

    when (val state = photoUiState) {
        is PhotoUiState.Success -> {
            PhotoScreen(
                currentPhotoUrl = photoUrl,
                photos = state.photoList,
                modifier = modifier,
                onNavigateBack = onNavigateBack,
            )
        }

        is PhotoUiState.Loading -> {
            PhotoScreen(
                currentPhotoUrl = photoUrl,
                photos = listOf(Photo(0, photoUrl)),
                modifier = modifier,
                onNavigateBack = onNavigateBack,
            )
        }

        is PhotoUiState.Error -> {
            PhotoScreen(
                currentPhotoUrl = photoUrl,
                photos = listOf(Photo(0, photoUrl)),
                modifier = modifier,
                onNavigateBack = onNavigateBack,
            )
        }
    }
}

@Composable
fun PhotoScreen(
    currentPhotoUrl: String,
    photos: List<Photo>,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    val pagerState =
        rememberPagerState(
            initialPage = photos.indexOfFirst { it.photoUrl == currentPhotoUrl },
            pageCount = { photos.size },
        )

    Box(
        modifier = modifier.fillMaxSize().background(White),
    ) {
        Column {
            TopAppBar(
                title = {
                },
                onNavigationClick = onNavigateBack,
            )
            Spacer(modifier = Modifier.fillMaxSize(0.02f))

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                ZoomableImage(
                    imageUrl = photos[page].photoUrl,
                )
            }
        }
    }
}

@Composable
fun ZoomableImage(imageUrl: String) {
    AsyncImage(
        modifier = Modifier.fillMaxSize(),
        model = imageUrl,
        contentDescription = "fullscreen_photo",
        contentScale = ContentScale.Fit,
    )
}

@Preview
@Composable
fun PhotoScreenPreview() {
    PhotoScreen(
        modifier = Modifier,
        currentPhotoUrl = "",
        photos = emptyList(),
        onNavigateBack = {},
    )
}
