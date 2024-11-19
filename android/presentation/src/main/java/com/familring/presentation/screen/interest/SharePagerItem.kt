package com.familring.presentation.screen.interest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.familring.presentation.component.ImageLoadingProgress
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun SharePagerItem(
    username: String = "",
    zodiacImg: String = "",
    imgUrl: String = "",
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = White, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 3.dp,
                    color = Gray03,
                    shape = RoundedCornerShape(12.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.85f)
                        .fillMaxHeight(0.83f)
                        .padding(top = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier =
                        Modifier
                            .clip(shape = RoundedCornerShape(12.dp)),
                    model = imgUrl,
                    contentDescription = "share_img",
                    contentScale = ContentScale.FillWidth,
                    onLoading = {
                        isImageLoaded = false
                    },
                    onSuccess = {
                        isImageLoaded = true
                    },
                )
                if (!isImageLoaded) {
                    ImageLoadingProgress()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    AsyncImage(
                        modifier =
                            Modifier
                                .padding(end = 3.dp)
                                .size(30.dp),
                        model = zodiacImg,
                        contentDescription = "zodiac",
                    )
                }
                Text(
                    text = "${username}의 인증샷",
                    style = Typography.headlineMedium.copy(fontSize = 20.sp),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun SharePagerItemPreview() {
    SharePagerItem()
}
