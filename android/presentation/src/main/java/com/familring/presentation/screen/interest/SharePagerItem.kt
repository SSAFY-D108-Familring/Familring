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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun SharePagerItem(
    username: String = "",
    zodiacImg: String = "",
    imgUrl: String = "",
) {
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
            AsyncImage(
                modifier =
                    Modifier
                        .fillMaxHeight(0.83f)
                        .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                model = imgUrl,
                contentDescription = "share_img",
                contentScale = ContentScale.Fit,
            )
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
                        modifier = Modifier.padding(end = 3.dp).size(30.dp),
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
