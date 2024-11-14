package com.familring.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.EmotionText
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import kotlinx.coroutines.launch

@Composable
fun EmotionUpdateScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    clickEmotion: (String) -> Unit,
) {
    val emotionList =
        listOf(
            EmotionText("🙂", "평범해요"),
            EmotionText("😆", "기뻐요"),
            EmotionText("😎", "즐거워요"),
            EmotionText("🤬", "화났어요"),
            EmotionText("😥", "슬퍼요"),
        )

    val pagerState = rememberPagerState { emotionList.size }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    // 선택된 페이지가 변경될 때 selectedEmotion을 업데이트
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedIndex = page
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
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
                    Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                    HorizontalPager(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f),
                        state = pagerState,
                        pageSize = PageSize.Fixed(150.dp),
                        contentPadding = PaddingValues(horizontal = 80.dp),
                        pageSpacing = (-20).dp,
                    ) { page ->
                        val emotion = emotionList[page]
                        EmotionItem(
                            emotion = emotion,
                            isSelected = emotion.emotion == emotionList[selectedIndex].emotion,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(page)
                                }
                            },
                        )
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(0.08f))
                    RoundLongButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "기분 저장하기",
                        onClick = {
                            val selectedEmotion = emotionList[selectedIndex].emotion
                            val selectedEmoji = emotionList[selectedIndex].emoji
                            clickEmotion("$selectedEmotion $selectedEmoji")
                            onClose()
                        },
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

@Composable
fun EmotionItem(
    emotion: EmotionText,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .alpha(if (isSelected) 1f else 0.5f)
                .noRippleClickable { onClick() }
                .size(150.dp)
                .background(
                    color = if (isSelected) Green01 else Green01.copy(alpha = 0.4f),
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = emotion.emoji,
                style = Typography.titleLarge.copy(fontSize = if (isSelected) 60.sp else 40.sp),
            )
            Spacer(modifier = Modifier.height(if (isSelected) 4.dp else 8.dp))
            Text(
                text = emotion.emotion,
                style = Typography.titleMedium.copy(fontSize = if (isSelected) 20.sp else 16.sp),
                color = Black,
            )
        }
    }
}

@Preview
@Composable
fun EmotionGridPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        EmotionUpdateScreen(clickEmotion = {})
    }
}
