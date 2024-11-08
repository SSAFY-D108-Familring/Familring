package com.familring.presentation.screen.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Photo
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import timber.log.Timber
import java.io.File

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
    val context = LocalContext.current
    val photoUiState by viewModel.photoUiState.collectAsStateWithLifecycle()
    val galleryUiEvent by viewModel.galleryUiEvent.collectAsStateWithLifecycle(GalleryUiEvent.Loading)

    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permission,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }

    val multiplePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10), // maxItems 추가
        ) { uris ->
            if (uris.isNotEmpty()) {
                val files =
                    uris.map { uri ->
                        val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            file.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        file
                    }
                viewModel.uploadPhotos(albumId, files)
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                hasPermission = true
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            } else {
                Toast
                    .makeText(
                        context,
                        "갤러리 접근을 위해서는 권한이 필요합니다. 설정에서 권한을 허용해주세요.",
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }

    val onAddPhotoClick = {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }

            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    LaunchedEffect(galleryUiEvent) {
        when (galleryUiEvent) {
            is GalleryUiEvent.Success -> {
                Toast.makeText(context, "사진이 성공적으로 업로드되었습니다", Toast.LENGTH_SHORT).show()
            }

            is GalleryUiEvent.Error -> {
                Toast.makeText(context, "사진 업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    LaunchedEffect(albumId) {
        viewModel.getOneAlbum(albumId)
    }

    Surface(modifier = modifier.fillMaxSize(), color = White) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text =
                            when (val state = photoUiState) {
                                is PhotoUiState.Success -> state.albumName
                                else -> "앨범"
                            },
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
                        Timber.d(state.photoList.size.toString())
                        item {
                            AddPhotoButton(onClick = onAddPhotoClick)
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
fun PhotoItem(photo: Photo) {
    AsyncImage(
        modifier = Modifier.aspectRatio(1f),
        model = photo.photoUrl,
        contentDescription = "photo_item_img",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun AddPhotoButton(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        border = BorderStroke(1.dp, Gray03),
        shape = RoundedCornerShape(18.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        colors =
            CardDefaults.cardColors(
                containerColor = White,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "+",
                style = Typography.headlineLarge.copy(fontSize = 60.sp),
                color = Gray03,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "앨범 추가",
                style = Typography.bodyMedium,
                color = Gray03,
            )
        }
    }
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
