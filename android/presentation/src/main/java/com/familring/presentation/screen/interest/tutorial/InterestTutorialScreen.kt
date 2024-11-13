package com.familring.presentation.screen.interest.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography

@Composable
fun InterestTutorialScreen(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val tutorialImages =
        listOf(
            R.drawable.img_tutorial_interest_first,
            R.drawable.img_tutorial_interest_second,
            R.drawable.img_tutorial_interest_third,
            R.drawable.img_tutorial_interest_fourth,
        )
    val maxHeight =
        with(
            LocalDensity.current,
        ) {
            tutorialImages.maxOfOrNull {
                painterResource(id = it).intrinsicSize.height.toDp()
            }
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(20.dp),
    ) {
        Text(
            text = "관심사 공유 미리보기  \uD83D\uDD0D",
            style = Typography.titleLarge.copy(fontSize = 25.sp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text =
                "가족들의 최근 관심사를 알아보고\n" +
                    "체험한 후 인증하며 가까워질 수 있어요 !",
            style =
                Typography.displayLarge.copy(
                    color = Gray01,
                    fontSize = 17.sp,
                ),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(maxHeight ?: 0.dp),
            state = pagerState,
        ) { page ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = tutorialImages[page]),
                    contentDescription = "img_tutorial_interest_first",
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Green02 else Gray03
                Box(
                    modifier =
                        Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .size(15.dp)
                            .background(color),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterestTutorialScreenPreview() {
    InterestTutorialScreen()
}
