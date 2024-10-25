package com.familring.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.R
import com.familring.presentation.theme.Black

@Composable
fun TimeCapsuleScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.img_rightwards_pushing_hand),
                contentDescription = "signup_emoji",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "잠깐! 오늘은 타임캡슐을 작성하는 날이 아니에요",
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f)) // 비율
            Text(
                text = "2024년 11월 1일에 작성할 수 있어요",
                style = Typography.headlineLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.height(10.dp)) // 고정값
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleScreenPreview() {
    TimeCapsuleScreen(Modifier)
}