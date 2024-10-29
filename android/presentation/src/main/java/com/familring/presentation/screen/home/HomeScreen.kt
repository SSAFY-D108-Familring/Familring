package com.familring.presentation.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Green04
import com.familring.presentation.theme.Green06
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

@Composable
fun HomeRoute(modifier: Modifier) {
    HomeScreen(modifier = modifier)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var progress by remember {
        mutableStateOf(0f)
    }
    val size by animateFloatAsState(
        targetValue = progress,
        tween(delayMillis = 200, durationMillis = 1000, easing = LinearOutSlowInEasing),
    )

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.fillMaxHeight(0.13f))
            Image(
                painter = painterResource(id = R.drawable.img_tree_center),
                contentDescription = "tree_center_background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
        }
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.fillMaxSize(0.02f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Image(
                    modifier =
                    Modifier
                        .size(24.dp)
                        .noRippleClickable {
                            // 알림 클릭 이벤트
                        },
                    painter = painterResource(id = R.drawable.img_home_notification),
                    contentDescription = "home notification img",
                )
                Spacer(modifier = Modifier.size(8.dp))
                Image(
                    modifier =
                    Modifier
                        .size(24.dp)
                        .noRippleClickable {
                            // 프로필 이벤트
                        },
                    painter = painterResource(id = R.drawable.img_profile_circle),
                    contentDescription = "profile circle img",
                )
            }

            Text(
                text = "우리 가족의 나무는 지금...",
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        if (progress > 0.69f) {
                            "숲들숲들🎄"
                        } else if (progress > 0.29f) {
                            "들숲들숲\uD83C\uDF84"
                        } else {
                            "시들시들\uD83C\uDF84"
                        },
                    style = Typography.titleLarge.copy(fontSize = 30.sp),
                    color = Green02,
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .padding(top = 12.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(15.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(Gray02),
                        )
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(size)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(
                                        if (progress > 0.69f) {
                                            Green02
                                        } else if (progress > 0.29f) {
                                            Green03
                                        } else {
                                            Green04
                                        },
                                    ).animateContentSize(),
                        )
                    }
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(size)
                                    .padding(end = 0.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                text = "${(progress * 100).toInt()}",
                                style = Typography.displaySmall.copy(fontSize = 8.sp),
                                color = Color.Black,
                            )
                        }

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 0.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                text = "100",
                                style = Typography.displaySmall.copy(fontSize = 8.sp),
                                color = Gray02,
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_pill),
                    contentDescription = "pill_img",
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.fillMaxSize(0.03f))
                Image(
                    painter = painterResource(id = R.drawable.img_networking),
                    contentDescription = "network_img",
                    modifier = Modifier.size(60.dp),
                )
            }

            Spacer(modifier = Modifier.fillMaxSize(0.4f))
            Text(text = "하이요")
            FamilyCard()
        }
    }

    LaunchedEffect(key1 = true) {
        progress = 0.7f
    }
}

@Composable
fun FamilyCard() {
    Card(
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(modifier = Modifier.background(color = Green06).padding(horizontal = 22.dp).padding(vertical = 9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "엄마밍", style = Typography.titleLarge.copy(fontSize = 15.sp))
            Spacer(modifier = Modifier.fillMaxSize(0.01f))
            Image(
                painter = painterResource(id = R.drawable.img_chicken),
                contentDescription = "chicken_img",
            )
            Spacer(modifier = Modifier.fillMaxSize(0.02f))
            Text(text = "화났어요 \uD83E\uDD2C", style = Typography.displaySmall.copy(fontSize = 11.sp))
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    FamilyCard()
}
