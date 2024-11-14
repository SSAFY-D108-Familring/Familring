package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.EmotionText
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun EmotionGrid(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    clickEmotion: (String) -> Unit,
) {
    val emotionList =
        listOf(
            EmotionText("😆", "기뻐요"),
            EmotionText("🙂", "평범해요"),
            EmotionText("😎", "즐거워요"),
            EmotionText("🤬", "화났어요"),
            EmotionText("😥", "슬퍼요"),
        )

    val pagerState = rememberPagerState(pageCount = { emotionList.size })
    var selectedEmotion by remember { mutableStateOf<EmotionText?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black.copy(alpha = 0.4f),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .noRippleClickable { onClose() },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(enabled = false) {},
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = White)
                            .padding(vertical = 25.dp, horizontal = 12.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "지금 기분이 어때요?",
                        color = Black,
                        style = Typography.titleSmall.copy(fontSize = 22.sp),
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "가족들에게 지금 내 기분을 알려봐요!",
                        color = Gray01,
                        style = Typography.bodyMedium.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(25.dp))

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                    ) {
                        HorizontalPager(
                            modifier = Modifier.fillMaxWidth(),
                            state = pagerState,
                        ) { page ->
                            val emotion = emotionList[page]
                            Box(
                                modifier =
                                    Modifier
                                        .padding(horizontal = 16.dp)
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                        .background(
                                            if (emotion == selectedEmotion) {
                                                Green02
                                            } else {
                                                Green01.copy(alpha = 0.25f)
                                            },
                                        ).clickable { selectedEmotion = emotion },
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = emotion.emoji,
                                        style = Typography.titleLarge.copy(fontSize = 60.sp),
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = emotion.emotion,
                                        style = Typography.titleMedium,
                                        color = if (emotion == selectedEmotion) White else Black,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    RoundLongButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "기분 저장하기",
                        onClick = {
                            selectedEmotion?.let { clickEmotion(it.emotion) }
                            onClose()
                        },
                        enabled = selectedEmotion != null,
                    )
                }

                Spacer(modifier = Modifier.height(13.dp))
                Text(
                    text = "빈 화면을 터치하면 닫혀요!",
                    color = White,
                    style = Typography.headlineSmall,
                )
            }
        }
    }
}

@Preview
@Composable
fun EmotionGridPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        EmotionGrid(clickEmotion = {})
    }
}
