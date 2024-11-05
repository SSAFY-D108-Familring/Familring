package com.familring.presentation.screen.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun QuestionListRoute(onNavigateBack: () -> Unit) {
    QuestionListScreen(onNavigateBack = onNavigateBack)
}

@Composable
fun QuestionListScreen(onNavigateBack: () -> Unit) {
    var isLatestSelected by remember { mutableStateOf(true) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_question_back),
            contentDescription = "background_img",
            modifier =
                Modifier
                    .fillMaxSize()
                    .alpha(0.25f),
            contentScale = ContentScale.FillBounds,
        )
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(text = "질문 목록", style = Typography.headlineMedium.copy(fontSize = 22.sp))
                },
                onNavigationClick = onNavigateBack,
            )
            Spacer(modifier = Modifier.fillMaxSize(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "최신순",
                    style = Typography.headlineSmall.copy(fontSize = 14.sp),
                    modifier =
                        Modifier
                            .background(
                                color = if (isLatestSelected) Green02 else White,
                                shape = RoundedCornerShape(30.dp),
                            ).border(
                                width = 1.dp,
                                color = if (!isLatestSelected) Gray03 else Color.Transparent,
                                shape = RoundedCornerShape(30.dp),
                            ).padding(horizontal = 19.dp, vertical = 8.dp)
                            .clickable { isLatestSelected = true },
                    color = if (isLatestSelected) White else Color.Black,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "오래된순",
                    style = Typography.headlineSmall.copy(fontSize = 14.sp),
                    modifier =
                        Modifier
                            .background(
                                color = if (!isLatestSelected) Green02 else White,
                                shape = RoundedCornerShape(30.dp),
                            ).border(
                                width = 1.dp,
                                color = if (isLatestSelected) Gray03 else Color.Transparent,
                                shape = RoundedCornerShape(30.dp),
                            ).padding(horizontal = 19.dp, vertical = 8.dp)
                            .noRippleClickable { isLatestSelected = false },
                    color = if (!isLatestSelected) White else Color.Black,
                )
            }
            Spacer(modifier = Modifier.fillMaxSize(0.03f))
            LazyColumn {
                items(10) {
                    QuestionListItem()
                }
            }
        }
    }
}

@Composable
fun QuestionListItem() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(bottom = 15.dp),
    ) {
        Text(text = "n번째 질문", style = Typography.labelSmall)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "네코타츠나는 꿈을 무슨 언어로 꿀까? 나와 이세돌 이야기 주르르 루트 원트 클",
            style = Typography.displayMedium,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(18.dp))
        Spacer(
            modifier =
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Gray03),
        )
    }
}

@Preview
@Composable
fun QuestionListPreview() {
    QuestionListScreen(onNavigateBack = {})
}
