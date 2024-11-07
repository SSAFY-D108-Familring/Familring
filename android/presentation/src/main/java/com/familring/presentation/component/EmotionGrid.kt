package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.EmotionText
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun EmotionGrid(
    modifier: Modifier = Modifier,
    clickEmotion: (String) -> Unit,
) {
    val emotionList =
        listOf(
            EmotionText("😆", "기뻐요 \uD83D\uDE06"),
            EmotionText("🙂", "평범해요 \uD83D\uDE42"),
            EmotionText("😎", "즐거워요 \uD83D\uDE0E"),
            EmotionText("🤬", "화났어요 \uD83E\uDD2C"),
            EmotionText("😥", "슬퍼요 \uD83D\uDE25"),
        )
    Column(
        modifier =
            Modifier
                .wrapContentSize()
                .background(color = White, shape = RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = "기분 선택",
            style = Typography.headlineMedium,
        )
        LazyVerticalGrid(
            modifier =
                modifier
                    .fillMaxWidth(0.8f),
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState(),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(emotionList.size) { index ->
                val item = emotionList[index]
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .border(width = 3.dp, color = Gray03, shape = RoundedCornerShape(14.dp))
                            .clickable { clickEmotion(item.emotion) }
                            .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = item.emoji, style = Typography.bodyLarge.copy(fontSize = 40.sp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.emotion,
                        style = Typography.bodyMedium,
                        color = Black,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
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
