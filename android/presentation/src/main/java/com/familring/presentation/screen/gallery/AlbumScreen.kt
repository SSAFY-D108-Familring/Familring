package com.familring.presentation.screen.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Photo
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
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
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedPhotos by remember { mutableStateOf(setOf<Long>()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10),
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
        if (!isLoading) {
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
    }

    LaunchedEffect(galleryUiEvent) {
        when (galleryUiEvent) {
            is GalleryUiEvent.Success -> {
                Toast.makeText(context, "성공적으로 반영되었습니다", Toast.LENGTH_SHORT).show()
            }

            is GalleryUiEvent.Error -> {
                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
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
                trailingIcon = {
                    when (val state = photoUiState) {
                        is PhotoUiState.Success -> {
                            if (state.photoList.isNotEmpty()) {
                                if (isSelectionMode) {
                                    Row {
                                        Icon(
                                            modifier =
                                                Modifier
                                                    .padding(end = 16.dp)
                                                    .noRippleClickable {
                                                        if (selectedPhotos.isNotEmpty()) {
                                                            showDeleteDialog = true
                                                        }
                                                    },
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "delete img",
                                            tint = Black,
                                        )
                                        Icon(
                                            modifier =
                                                Modifier.noRippleClickable {
                                                    isSelectionMode = false
                                                    selectedPhotos = emptySet()
                                                },
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "close",
                                            tint = Black,
                                        )
                                    }
                                } else {
                                    Text(
                                        modifier =
                                            Modifier
                                                .padding(end = 2.dp)
                                                .noRippleClickable {
                                                    isSelectionMode = true
                                                },
                                        text = "선택",
                                        style = Typography.headlineLarge.copy(fontSize = 20.sp),
                                        color = Black,
                                    )
                                }
                            }
                        }

                        else -> {}
                    }
                },
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

                is PhotoUiState.Success -> {
                    if (state.photoList.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_box),
                                contentDescription = "emptyBox",
                            )
                            Spacer(modifier = Modifier.fillMaxSize(0.01f))
                            Text(
                                text = "앨범이 비어있어요!",
                                style = Typography.titleLarge.copy(fontSize = 26.sp),
                            )
                            Spacer(modifier = Modifier.fillMaxSize(0.01f))
                            Text(
                                text = "하단 버튼을 클릭해서\n우리 가족의 추억을 기록해봐요!",
                                style =
                                    Typography.bodyMedium.copy(
                                        fontSize = 20.sp,
                                        color = Gray02,
                                    ),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.fillMaxSize(0.05f))
                            RoundLongButton(
                                backgroundColor = Green02,
                                text = "사진 추가하기",
                                enabled = !isLoading,
                                onClick = onAddPhotoClick,
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.padding(2.dp),
                            columns = GridCells.Fixed(4),
                            state = rememberLazyGridState(),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            if (!isSelectionMode) {
                                item {
                                    AddPhotoButton(
                                        onClick = onAddPhotoClick,
                                    )
                                }
                            }
                            items(state.photoList.size) { index ->
                                PhotoItem(
                                    photo = state.photoList[index],
                                    selected = state.photoList[index].id in selectedPhotos,
                                    onPhotoClick = { photo ->
                                        if (isSelectionMode && !isLoading) {
                                            selectedPhotos =
                                                if (photo.id in selectedPhotos) {
                                                    selectedPhotos - photo.id
                                                } else {
                                                    selectedPhotos + photo.id
                                                }
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showDeleteDialog) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = Black.copy(0.5f))
                        .noRippleClickable { showDeleteDialog = false },
                contentAlignment = Alignment.Center,
            ) {
                TwoButtonTextDialog(
                    text = "정말 사진을 삭제하시겠습니까?",
                    onConfirmClick = {
                        showDeleteDialog = false
                        viewModel.deletePhotos(albumId, selectedPhotos.toList())
                        isSelectionMode = false
                        selectedPhotos = emptySet()
                    },
                    onDismissClick = {
                        showDeleteDialog = false
                    },
                )
            }
        }

        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                // 로딩추가
                CircularProgressIndicator(color = Green02)
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    selected: Boolean,
    onPhotoClick: (Photo) -> Unit,
) {
    Box {
        AsyncImage(
            modifier =
                Modifier
                    .aspectRatio(1f)
                    .noRippleClickable { onPhotoClick(photo) },
            model = photo.photoUrl,
            contentDescription = "photo_item_img",
            contentScale = ContentScale.Crop,
        )
        if (selected) {
            Box(
                modifier =
                    Modifier
                        .aspectRatio(1f)
                        .background(color = Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
fun AddPhotoButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .noRippleClickable { onClick() }
                .fillMaxWidth()
                .background(color = Gray04)
                .aspectRatio(1f),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "+",
                style = Typography.headlineLarge.copy(fontSize = 45.sp),
                color = Gray02,
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
