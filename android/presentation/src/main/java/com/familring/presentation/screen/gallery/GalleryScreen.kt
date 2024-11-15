package com.familring.presentation.screen.gallery

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Album
import com.familring.domain.model.gallery.AlbumType
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.TutorialScreen
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryRoute(
    modifier: Modifier,
    navigateToAlbum: (Long, Boolean) -> Unit,
    viewModel: GalleryViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
) {
    val tutorialUiState by viewModel.tutorialUiState.collectAsStateWithLifecycle()

    val galleryUiState by viewModel.galleryUiState.collectAsStateWithLifecycle()
    val galleryUiEvent by viewModel.galleryUiEvent.collectAsStateWithLifecycle(GalleryUiEvent.Init)

    var showLoading by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTutorial by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getAlbums(listOf(AlbumType.NORMAL, AlbumType.SCHEDULE, AlbumType.PERSON))
    }

    GalleryScreen(
        modifier = modifier,
        navigateToAlbum = navigateToAlbum,
        galleryUiState = galleryUiState,
        onGalleryChange = { isNormal ->
            viewModel.getAlbums(
                if (isNormal) {
                    listOf(
                        AlbumType.NORMAL,
                        AlbumType.SCHEDULE,
                    )
                } else {
                    listOf(AlbumType.PERSON)
                },
            )
        },
        onGalleryCreate = { albumName, albumType ->
            viewModel.createAlbum(null, albumName, albumType)
        },
        onUpdateAlbum = { albumId, albumName ->
            viewModel.updateAlbum(albumId, albumName)
        },
        deleteAlbum = { albumId ->
            viewModel.deleteAlbum(albumId)
        },
        showTutorial = {
            showTutorial = true
            viewModel.setReadTutorialState(false)
        },
    )

    LaunchedEffect(galleryUiEvent) {
        when (galleryUiEvent) {
            is GalleryUiEvent.Init -> {
                showLoading = false
            }

            is GalleryUiEvent.Loading -> {
                showLoading = true
            }

            is GalleryUiEvent.Success -> {
                showLoading = false
                showSnackBar("앨범이 생성되었습니다")
            }

            is GalleryUiEvent.Error -> {
                showLoading = false
                showSnackBar("에러가 발생했습니다")
            }
        }
    }

    if (showLoading) {
        LoadingDialog(loadingMessage = "앨범 생성중...")
    }

    if (showTutorial && !tutorialUiState.isReadTutorial) {
        ModalBottomSheet(
            containerColor = White,
            onDismissRequest = {
                showTutorial = false
                viewModel.setReadTutorial()
            },
            sheetState = sheetState,
        ) {
            TutorialScreen(
                imageLists =
                    listOf(
                        R.drawable.img_tutorial_album_first,
                        R.drawable.img_tutorial_album_second,
                        R.drawable.img_tutorial_album_third,
                        R.drawable.img_tutorial_album_fourth,
                    ),
                title = "공유 앨범 미리보기 \uD83D\uDD0D",
                subTitle =
                    "가족끼리 일정에 관련된 앨범을 공유하고\n" +
                        "자신 얼굴이 들어간 사진을 자동으로 분류해줘요!",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    modifier: Modifier,
    navigateToAlbum: (Long, Boolean) -> Unit,
    galleryUiState: GalleryUiState,
    onGalleryChange: (Boolean) -> Unit,
    onGalleryCreate: (String, AlbumType) -> Unit = { _, _ -> },
    onUpdateAlbum: (Long, String) -> Unit = { _, _ -> },
    deleteAlbum: (Long) -> Unit = {},
    showTutorial: () -> Unit = {},
) {
    var privateGallerySelected by rememberSaveable { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var albumname by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(privateGallerySelected) {
        onGalleryChange(privateGallerySelected)
    }

    Surface(modifier = modifier.fillMaxSize(), color = White) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "앨범",
                        style = Typography.titleLarge,
                        color = Black,
                    )
                },
                tutorialIcon = {
                    Icon(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .border(
                                    width = 2.dp,
                                    color = Gray03,
                                    shape = CircleShape,
                                ).padding(2.dp)
                                .noRippleClickable { showTutorial() },
                        painter = painterResource(id = R.drawable.ic_tutorial),
                        contentDescription = "ic_question",
                        tint = Gray03,
                    )
                },
                navigationType = TopAppBarNavigationType.None,
            )
            Spacer(modifier = Modifier.fillMaxSize(0.03f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "공유",
                    style = Typography.headlineSmall.copy(fontSize = 14.sp),
                    modifier =
                        Modifier
                            .background(
                                color = if (privateGallerySelected) Green02 else White,
                                shape = RoundedCornerShape(30.dp),
                            ).border(
                                border =
                                    if (privateGallerySelected) {
                                        BorderStroke(0.dp, Gray03)
                                    } else {
                                        BorderStroke(1.dp, Gray03)
                                    },
                                RoundedCornerShape(30.dp),
                            ).noRippleClickable {
                                privateGallerySelected = true
                            }.padding(horizontal = 19.dp, vertical = 8.dp),
                    color = if (privateGallerySelected) White else Color.Black,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "인물",
                    style = Typography.headlineSmall.copy(fontSize = 14.sp),
                    modifier =
                        Modifier
                            .background(
                                color = if (!privateGallerySelected) Green02 else White,
                                shape = RoundedCornerShape(30.dp),
                            ).border(
                                border =
                                    if (!privateGallerySelected) {
                                        BorderStroke(0.dp, Gray03)
                                    } else {
                                        BorderStroke(1.dp, Gray03)
                                    },
                                RoundedCornerShape(30.dp),
                            ).noRippleClickable {
                                privateGallerySelected = false
                            }.padding(horizontal = 19.dp, vertical = 8.dp),
                    color = if (!privateGallerySelected) White else Color.Black,
                )
            }
            Spacer(modifier = Modifier.fillMaxSize(0.04f))
            when (galleryUiState) {
                is GalleryUiState.Loading -> {
                    showLoading = true
                }

                is GalleryUiState.Success -> {
                    showLoading = false
                    LazyVerticalGrid(
                        modifier = Modifier.padding(16.dp),
                        columns = GridCells.Fixed(2),
                        state = rememberLazyGridState(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val albums =
                            if (privateGallerySelected) {
                                galleryUiState.normalAlbums
                            } else {
                                galleryUiState.personAlbums
                            }

                        items(albums.size) { index ->
                            GalleryItem(
                                albums[index],
                                navigateToAlbum = navigateToAlbum,
                                onUpdateAlbum = onUpdateAlbum,
                                deleteAlbum = deleteAlbum,
                                isNormal = privateGallerySelected,
                            )
                        }
                        if (privateGallerySelected) {
                            item {
                                AddAlbumButton(onClick = {
                                    showBottomSheet = true
                                })
                            }
                        }
                    }
                }

                is GalleryUiState.Error -> {
                    showLoading = false
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = galleryUiState.errorMessage,
                            style = Typography.bodyLarge,
                            color = Color.Red,
                        )
                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = White,
                    onDismissRequest = {
                        showBottomSheet = false
                        albumname = ""
                    },
                ) {
                    Column(
                        modifier =
                            Modifier
                                .background(color = White)
                                .padding(top = 16.dp),
                    ) {
                        BasicTextField(
                            modifier =
                                Modifier
                                    .background(Color.Transparent)
                                    .padding(horizontal = 26.dp),
                            value = albumname,
                            onValueChange = { albumname = it },
                            textStyle =
                                Typography.titleSmall.copy(
                                    color = if (albumname.isEmpty()) Gray03 else Color.Black,
                                    fontSize = 24.sp,
                                ),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (albumname.isEmpty()) {
                                        Text(
                                            text = "앨범 이름",
                                            color = Gray03,
                                            style = Typography.titleSmall.copy(fontSize = 24.sp),
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        RoundLongButton(
                            backgroundColor = Green02,
                            text = "생성",
                            enabled = albumname.isNotEmpty(),
                            onClick = {
                                Timber.d("생성하기")
                                if (privateGallerySelected) {
                                    onGalleryCreate(albumname, AlbumType.NORMAL)
                                } else {
                                    onGalleryCreate(albumname, AlbumType.PERSON)
                                }
                                showBottomSheet = false
                            },
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
        if (showLoading) {
            LoadingDialog(loadingMessage = "앨범 로딩중...")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryItem(
    album: Album,
    navigateToAlbum: (Long, Boolean) -> Unit,
    onUpdateAlbum: (Long, String) -> Unit,
    deleteAlbum: (Long) -> Unit,
    isNormal: Boolean,
) {
    var showDialog by remember { mutableStateOf(false) }
    var updatedAlbumName by remember { mutableStateOf(album.albumName) }

    if (showDialog && isNormal) {
        ModalBottomSheet(
            containerColor = White,
            onDismissRequest = {
                showDialog = false
                updatedAlbumName = album.albumName // 취소 시 원래 이름으로 초기화
            },
        ) {
            Column(
                modifier =
                    Modifier
                        .background(color = White)
                        .padding(top = 16.dp),
            ) {
                BasicTextField(
                    modifier =
                        Modifier
                            .background(Color.Transparent)
                            .padding(horizontal = 26.dp),
                    value = updatedAlbumName,
                    onValueChange = { updatedAlbumName = it },
                    // 처음엔 그레이 색..?
                    textStyle =
                        Typography.titleSmall.copy(
                            color = if (updatedAlbumName.isEmpty()) Gray03 else Color.Black,
                            fontSize = 24.sp,
                        ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (updatedAlbumName.isEmpty()) {
                                Text(
                                    text = "앨범 이름",
                                    color = Gray03,
                                    style = Typography.titleSmall.copy(fontSize = 24.sp),
                                )
                            }
                            innerTextField()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                    ) {
                        RoundLongButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = Green02,
                            text = "수정",
                            onClick = {
                                if (updatedAlbumName.isNotEmpty()) {
                                    onUpdateAlbum(album.id, updatedAlbumName)
                                    showDialog = false
                                }
                            },
                            fraction = 1f,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        RoundLongButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = Green02,
                            text = "삭제",
                            onClick = {
                                deleteAlbum(album.id)
                                showDialog = false
                            },
                            fraction = 1f,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .then(
                        if (isNormal) {
                            Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { showDialog = true },
                                    onTap = {
                                        navigateToAlbum(album.id, true)
                                        Timber.d("짧터치")
                                    },
                                )
                            }
                        } else {
                            Modifier.noRippleClickable {
                                navigateToAlbum(album.id, false)
                            }
                        },
                    ),
            shape = RoundedCornerShape(18.dp),
        ) {
            AsyncImage(
                modifier = Modifier.aspectRatio(1f),
                model = album.thumbnailUrl,
                contentDescription = "gallery_main_img",
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = album.albumName,
            style = Typography.displaySmall.copy(fontSize = 15.sp),
            modifier = Modifier.padding(horizontal = 6.dp),
        )
        Text(
            text = album.photoCount.toString(),
            style = Typography.bodySmall.copy(fontSize = 15.sp),
            modifier = Modifier.padding(horizontal = 6.dp),
            color = Gray01,
        )
    }
}

@Composable
fun AddAlbumButton(onClick: () -> Unit) {
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
fun GalleryScreenPreview() {
    GalleryScreen(
        modifier = Modifier,
        navigateToAlbum = { _, _ -> },
        galleryUiState = GalleryUiState.Loading,
        onGalleryChange = {},
    )
}
