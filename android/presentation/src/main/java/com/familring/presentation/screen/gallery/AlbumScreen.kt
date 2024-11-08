package com.familring.presentation.screen.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Photo
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun AlbumRoute(
    albumId: Long,
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    viewModel: GalleryViewModel = hiltViewModel(),
) {
    AlbumScreen(
        albumId = albumId,
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        viewModel = viewModel,
    )
}

@Composable
fun AlbumScreen(
    albumId: Long,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: GalleryViewModel,
) {
    val photoUiState by viewModel.photoUiState.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.getOneAlbum(albumId)
    }

    Surface(modifier = modifier.fillMaxSize(), color = White) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "앨범이름",
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = onNavigateBack,
            )
            Spacer(modifier = Modifier.fillMaxSize(0.05f))
            when (val state = photoUiState) {
                is PhotoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Green02)
                    }
                }

                is PhotoUiState.Success -> {
                    LazyVerticalGrid(
                        modifier = Modifier.padding(2.dp),
                        columns = GridCells.Fixed(4),
                        state = rememberLazyGridState(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        items(state.photoList.size) { index ->
                            PhotoItem(state.photoList[index])
                        }
                    }
                }

                is PhotoUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = state.errorMessage,
                            style = Typography.bodyLarge,
                            color = Color.Red,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
) {
    AsyncImage(
        modifier = Modifier.aspectRatio(1f),
        model = photo.photoUrl,
        contentDescription = "photo_item_img",
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(
        albumId = 1L,
        modifier = Modifier,
        onNavigateBack = {},
        viewModel = hiltViewModel(),
    )
}
