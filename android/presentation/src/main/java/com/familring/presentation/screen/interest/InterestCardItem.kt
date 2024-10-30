package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun InterestCardItem(
    modifier: Modifier = Modifier,
    profileImage: Int = R.drawable.img_smile_face,
    nickname: String = "나갱이",
    interest: String = "엘지트윈스",
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .background(color = White, shape = RoundedCornerShape(16.dp))
                .border(width = 3.dp, color = Gray03, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.35f)
                        .aspectRatio(1f),
                painter = painterResource(id = profileImage),
                contentDescription = "profile",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.13f))
            Text(
                text = nickname,
                style = Typography.bodySmall.copy(fontSize = 17.sp),
                color = Gray01,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = interest,
                style = Typography.titleMedium.copy(fontSize = 25.sp),
                color = Green02,
            )
        }
    }
}

@Preview
@Composable
fun InterestCardItemPreview() {
    InterestCardItem()
}
