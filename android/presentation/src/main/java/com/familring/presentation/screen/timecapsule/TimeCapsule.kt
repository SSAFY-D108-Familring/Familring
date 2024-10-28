package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.Profile
import com.familring.domain.TimeCapsuleMessage
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography

@Composable
fun TimeCapsuleDialog(
    modifier: Modifier = Modifier,
    timeCapsuleMessages: List<TimeCapsuleMessage>,
) {
    val pagerState = rememberPagerState(pageCount = { timeCapsuleMessages.size })

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->
            TimeCapsule(
                timeCapsuleMessage = timeCapsuleMessages[page],
            )
        }
    }
}

@Composable
fun TimeCapsule(
    modifier: Modifier = Modifier,
    timeCapsuleMessage: TimeCapsuleMessage,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
        Text(
            text = "2024년 10월 7일 타임캡슐",
            style = Typography.headlineLarge.copy(fontSize = 20.sp),
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ZodiacBackgroundProfile(profile = timeCapsuleMessage.profile)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timeCapsuleMessage.profile.nickName,
                style =
                    Typography.titleMedium.copy(
                        fontSize = 22.sp,
                        color = Green03,
                    ),
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.02f))
        Text(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
            text = timeCapsuleMessage.message,
            style =
                Typography.labelMedium.copy(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                ),
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun TimeCapsuleDialogPreview() {
    TimeCapsuleDialog(
        timeCapsuleMessages =
            listOf(
                TimeCapsuleMessage(
                    id = 1,
                    profile =
                        Profile(
                            nickName = "엄마미",
                            zodiacImgUrl = "url",
                            backgroundColor = "#FEE222",
                        ),
                    message =
                        "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
                            "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
                            "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
                            "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
                ),
                TimeCapsuleMessage(
                    id = 1,
                    profile =
                        Profile(
                            nickName = "아빠미",
                            zodiacImgUrl = "url",
                            backgroundColor = "#FEA222",
                        ),
                    message =
                        "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
                            "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
                            "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
                            "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
                ),
            ),
    )
}

// @Preview(showBackground = true)
// @Composable
// private fun TimeCapsulePreview() {
//    TimeCapsule(
//        timeCapsuleMessage =
//            TimeCapsuleMessage(
//                id = 1,
//                profile =
//                    Profile(
//                        nickName = "엄마미",
//                        zodiacImgUrl = "url",
//                        backgroundColor = "#FEE222",
//                    ),
//                message =
//                    "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
//                        "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
//                        "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
//                        "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
//            ),
//    )
// }
