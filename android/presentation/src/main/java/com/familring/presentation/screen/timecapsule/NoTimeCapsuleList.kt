package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun NoTimeCapsuleList(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Image(
                painter = painterResource(id = R.drawable.img_worried_face),
                contentDescription = "smile_face",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f)) // 비율
            Text(
                text = "아직 생성된 타임캡슐이 없어요",
                style = Typography.headlineLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.018f))
            Text(
                text = "타임캡슐을 생성해 가족과 작성해 보세요!",
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
private fun NoTimeCapsuleListScreenPreview() {
    NoTimeCapsuleList(
        modifier = Modifier,
    )
}
