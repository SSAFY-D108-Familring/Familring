package com.familring.presentation.screen.gallery

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.gallery.Album
import com.familring.domain.model.gallery.AlbumType
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun GalleryRoute(
    modifier: Modifier,
    navigateToAlbum: () -> Unit,
    viewModel: GalleryViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
) {
    val galleryUiState by viewModel.galleryUiState.collectAsStateWithLifecycle()
    val galleryUiEvent by viewModel.galleryUiEvent.collectAsStateWithLifecycle(GalleryUiEvent.Loading)
    LaunchedEffect(Unit) {
        viewModel.getAlbums(listOf(AlbumType.NORMAL, AlbumType.PERSON))
    }

    GalleryScreen(
        modifier = modifier,
        navigateToAlbum = navigateToAlbum,
        galleryUiState = galleryUiState,
        onGalleryChange = { isNormal ->
            viewModel.getAlbums(listOf(if (isNormal) AlbumType.NORMAL else AlbumType.PERSON))
        },
        onGalleryCreate = { albumName, albumType ->
            viewModel.createAlbum(null, albumName, albumType)
        },
    )

    LaunchedEffect(Unit) {
        when (galleryUiEvent) {
            is GalleryUiEvent.Loading -> {
                // 로딩중
            }

            is GalleryUiEvent.Success -> {
                showSnackBar("앨범이 생성되었습니다")
            }

            is GalleryUiEvent.Error -> {
                showSnackBar("에러가 발생했습니다")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    modifier: Modifier,
    navigateToAlbum: () -> Unit,
    galleryUiState: GalleryUiState,
    onGalleryChange: (Boolean) -> Unit,
    onGalleryCreate: (String, AlbumType) -> Unit = { _, _ -> },
) {
    var privateGallerySelected by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var albumname by remember { mutableStateOf("") }

    LaunchedEffect(privateGallerySelected) {
        onGalleryChange(privateGallerySelected)
    }

    Surface(modifier = modifier.fillMaxSize(), color = White) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = { Text(text = "앨범", style = Typography.titleLarge) },
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
                                        BorderStroke(
                                            0.dp,
                                            Gray03,
                                        )
                                    } else {
                                        BorderStroke(1.dp, Gray03)
                                    },
                                RoundedCornerShape(30.dp),
                            ).noRippleClickable { privateGallerySelected = true }
                            .padding(horizontal = 19.dp, vertical = 8.dp),
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
                                        BorderStroke(
                                            0.dp,
                                            Gray03,
                                        )
                                    } else {
                                        BorderStroke(1.dp, Gray03)
                                    },
                                RoundedCornerShape(30.dp),
                            ).noRippleClickable { privateGallerySelected = false }
                            .padding(horizontal = 19.dp, vertical = 8.dp),
                    color = if (!privateGallerySelected) White else Color.Black,
                )
            }
            Spacer(modifier = Modifier.fillMaxSize(0.04f))
            when (galleryUiState) {
                is GalleryUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Green02)
                    }
                }

                is GalleryUiState.Success -> {
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
                            GalleryItem(albums[index], navigateToAlbum)
                        }
                        item {
                            AddAlbumButton(onClick = {
                                showBottomSheet = true
                            })
                        }
                    }
                }

                is GalleryUiState.Error -> {
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
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet = false
                    albumname = ""
                }) {
                    Column(
                        modifier =
                            Modifier
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

                        Spacer(modifier = Modifier.fillMaxSize(0.05f))
                        RoundLongButton(
                            backgroundColor = Brown01,
                            text = "생성하기",
                            onClick = {
                                Log.d("Gallery", "생성하기")
                                if (privateGallerySelected) {
                                    onGalleryCreate(albumname, AlbumType.NORMAL)
                                } else {
                                    onGalleryCreate(albumname, AlbumType.PERSON)
                                }
                                showBottomSheet = false
                            },
                        )
                        Spacer(modifier = Modifier.fillMaxSize(0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryItem(
    album: Album,
    navigateToAlbum: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
    ) {
        Card(
            onClick = { navigateToAlbum() },
            shape = RoundedCornerShape(18.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
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
        navigateToAlbum = {},
        galleryUiState = GalleryUiState.Loading,
        onGalleryChange = {},
    )
}
