package com.familring.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun BlockScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = White),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_interest_heart),
                contentDescription = "block",
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "지금은 패밀링 팀이 열심히 개발 중입니다!",
                color = Red01,
                style = Typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "완성이 되면 만나요 🫶",
                color = Black,
                style = Typography.headlineLarge,
            )
        }
    }
}
