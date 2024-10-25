package com.familring.presentation.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.FamilringTheme
import com.familring.presentation.theme.Typography

@Composable
fun LoginScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val pagerState =
                    rememberPagerState(pageCount = {
                        4
                    })

                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                Row(
                    modifier =
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier =
                                Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .size(10.dp)
                                    .background(color),
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "'오늘 치킨 먹을 사람은 찬성!'",
                            style = Typography.headlineLarge,
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "채팅",
                                style = Typography.headlineLarge,
                            )
                            Text(
                                text = "에서 간단히 저녁 메뉴를 정해 보세요",
                                style = Typography.labelLarge.copy(fontSize = 24.sp),
                            )
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                        Image(
                            painter = painterResource(id = R.drawable.img_img_leg),
                            contentDescription = "leg_img",
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.img_img_kakao_login),
                contentDescription = "img_kakao_login",
                modifier =
                    Modifier
                        .padding(bottom = 50.dp)
                        .clickable {
                            Log.d("login", "카카오 로그인")
                        },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FamilringTheme {
        LoginScreen()
    }
}
