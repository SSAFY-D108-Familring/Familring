package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun SharePagerItem(
    imageUri: Int,
    username: String,
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
            Image(
                modifier =
                    Modifier
                        .fillMaxHeight(0.83f)
                        .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                painter = painterResource(id = imageUri),
                contentDescription = "share_img",
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = username,
                style = Typography.headlineMedium.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
