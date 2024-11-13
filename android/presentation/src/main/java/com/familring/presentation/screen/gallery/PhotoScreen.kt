package com.familring.presentation.screen.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.White

@Composable
fun PhotoRoute(
    photoUrl: String,
    albumId: Long,
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
        modifier = modifier.fillMaxSize(),
    ) {
        // 배경을 검은색으로
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(White),
        )

        // 이미지 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            ZoomableImage(
                imageUrl = photos[page].photoUrl,
            )
        }

        // 상단 바
        TopAppBar(
            title = {
            },
            onNavigationClick = onNavigateBack,
        )
    }
}

@Composable
fun ZoomableImage(imageUrl: String) {
    AsyncImage(
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
