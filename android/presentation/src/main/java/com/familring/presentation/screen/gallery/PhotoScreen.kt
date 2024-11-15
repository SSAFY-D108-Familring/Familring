package com.familring.presentation.screen.gallery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.familring.domain.model.gallery.Photo
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun PhotoRoute(
    albumId: Long,
    photoUrl: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    showSnackbar: (String) -> Unit,
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
                showSnackbar = showSnackbar,
                viewModel = viewModel,
            )
        }

        is PhotoUiState.Loading -> {
            PhotoScreen(
                currentPhotoUrl = photoUrl,
                photos = listOf(Photo(0, photoUrl)),
                modifier = modifier,
                onNavigateBack = onNavigateBack,
                showSnackbar = showSnackbar,
                viewModel = viewModel,
            )
        }

        is PhotoUiState.Error -> {
            PhotoScreen(
                currentPhotoUrl = photoUrl,
                photos = listOf(Photo(0, photoUrl)),
                modifier = modifier,
                onNavigateBack = onNavigateBack,
                viewModel = viewModel,
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
    showSnackbar: (String) -> Unit = {},
    viewModel: GalleryViewModel,
    context: Context = LocalContext.current,
) {
    val pagerState =
        rememberPagerState(
            initialPage = photos.indexOfFirst { it.photoUrl == currentPhotoUrl },
            pageCount = { photos.size },
        )

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                viewModel.downloadImage(photos[pagerState.currentPage].photoUrl)
                showSnackbar("다운로드를 시작합니다.")
            } else {
                showSnackbar("갤러리 접근 권한이 필요합니다.")
            }
        }

    Box(
        modifier = modifier.fillMaxSize().background(White),
    ) {
        Column {
            TopAppBar(
                title = {
                },
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.img_img_down),
                        contentDescription = "down_image",
                        modifier = Modifier.padding(end = 10.dp).size(25.dp).noRippleClickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                viewModel.downloadImage(photos[pagerState.currentPage].photoUrl)
                                showSnackbar("다운로드를 시작합니다.")
                            } else {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    ),
                                    -> {
                                        viewModel.downloadImage(photos[pagerState.currentPage].photoUrl)
                                        showSnackbar("다운로드를 시작합니다.")
                                    }

                                    else -> {
                                        requestPermissionLauncher.launch(
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        )
                                    }
                                }
                            }
                        }
                    )
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
        viewModel = viewModel(),
    )
}
