package com.familring.presentation.screen.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

@Composable
fun AlbumRoute(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
) {
    AlbumScreen(modifier = modifier, onNavigateBack = onNavigateBack)
}

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    var photoCount by remember {
        mutableStateOf(10)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.fillMaxSize(0.02f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_meue_left),
                    contentDescription = "menu_left_img",
                    modifier =
                        Modifier
                            .size(25.dp)
                            .noRippleClickable {
                                onNavigateBack()
                            },
                )
                Spacer(modifier = Modifier.fillMaxSize(0.02f))
                Text(text = "앨범이름", style = Typography.headlineMedium.copy(fontSize = 22.sp))
            }
            Spacer(modifier = Modifier.fillMaxSize(0.05f))
            LazyVerticalGrid(
                modifier = Modifier.padding(2.dp),
                columns = GridCells.Fixed(4),
                state = rememberLazyGridState(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(photoCount) { PhotoItem() }
            }
        }
    }
}

@Composable
fun PhotoItem() {
    Image(
        modifier = Modifier.aspectRatio(1f),
        painter = painterResource(id = R.drawable.tuna),
        contentDescription = "photo_item_img",
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
fun AlbumScreenPreview() {
    AlbumScreen()
}
