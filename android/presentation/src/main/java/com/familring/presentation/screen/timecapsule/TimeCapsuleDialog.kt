package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.familring.domain.model.timecapsule.TimeCapsuleMessage
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.component.dialog.OneButtonTextDialog
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.toLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TimeCapsuleDialog(
    modifier: Modifier = Modifier,
    leftDays: Int = 0,
    timeCapsuleMessages: List<TimeCapsuleMessage> = listOf(),
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        if (leftDays > 0) {
            OneButtonTextDialog(text = "아직 캡슐을 열 수 없어요!", buttonText = "확인") {
                onDismiss()
            }
        } else if (timeCapsuleMessages.isEmpty()) {
            OneButtonTextDialog(text = "작성된 캡슐이 없어요", buttonText = "확인") {
                onDismiss()
            }
        } else {
            TimeCapsulePager(
                modifier =
                    modifier
                        .fillMaxHeight(0.6f),
                timeCapsuleMessages = timeCapsuleMessages,
            )
        }
    }
}

@Composable
fun TimeCapsulePager(
    modifier: Modifier = Modifier,
    timeCapsuleMessages: List<TimeCapsuleMessage> = listOf(),
) {
    val pagerState = rememberPagerState(pageCount = { timeCapsuleMessages.size })

    Column(
        modifier =
            modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            modifier =
                Modifier
                    .fillMaxWidth(),
            state = pagerState,
            pageSpacing = 14.dp,
            contentPadding = PaddingValues(horizontal = 28.dp),
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
                .clip(RoundedCornerShape(12.dp))
                .background(color = White)
                .padding(20.dp),
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.02f))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = timeCapsuleMessage.createdAt.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 타임캡슐")),
            style = Typography.headlineLarge.copy(fontSize = 20.sp),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ZodiacBackgroundProfile(
                profile = timeCapsuleMessage.profile,
                size = 90,
                paddingValue = 15,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = timeCapsuleMessage.profile.nickname,
                style =
                    Typography.titleMedium.copy(
                        fontSize = 22.sp,
                        color = Green03,
                    ),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
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
private fun TimeCapsulePagerPreview() {
    TimeCapsulePager(
        timeCapsuleMessages =
            listOf(
                TimeCapsuleMessage(
                    userNickname = "엄마미",
                    userZodiacSign = "url",
                    userColor = "0xFFFEE222",
                    message =
                        "적어놓을 것이오!!!!!!",
                    createdAt = "2024-10-05".toLocalDate(),
                ),
                TimeCapsuleMessage(
                    userNickname = "엄마미",
                    userZodiacSign = "url",
                    userColor = "0xFFFEE222",
                    message =
                        "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
                            "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
                            "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
                            "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
                    createdAt = "2024-10-06".toLocalDate(),
                ),
            ),
    )
}
