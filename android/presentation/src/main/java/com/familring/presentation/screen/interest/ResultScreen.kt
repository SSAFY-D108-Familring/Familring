package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    result: String = "",
    nickname: String = "",
    navigateToPeriod: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "관심사",
                style = Typography.titleSmall.copy(fontSize = 22.sp),
                color = Green03,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "가 선정되었네요",
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
        }
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "모두 축하해 주세요!",
            style = Typography.bodyLarge.copy(fontSize = 22.sp),
            color = Black,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "이번에 선정된 관심사는",
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${nickname}의 $result!",
                style = Typography.titleLarge,
                color = Green03,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.img_celebrate),
                contentDescription = "celebrate",
            )
            Spacer(modifier = Modifier.weight(7f))
            RoundLongButton(
                text = "인증 기간 지정하러 가기",
                onClick = navigateToPeriod,
            )
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    ResultScreen(
        nickname = "나갱",
        result = "엘지 트윈스",
    )
}
