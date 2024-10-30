package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun FinishedTimeCapsule(
    modifier: Modifier = Modifier,
    leftDay: Int = 0,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.img_smile_face),
                contentDescription = "smile_face",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f)) // 비율
            Text(
                text = "타임캡슐을 이미 작성했어요!",
                style = Typography.headlineLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "캡슐 오픈까지 ${leftDay}일!\n조금만 더 기다려보아요",
                style =
                    Typography.labelLarge.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun FinishedTimeCapsuleScreenPreview() {
    FinishedTimeCapsule(
        modifier = Modifier,
        leftDay = 3,
    )
}
