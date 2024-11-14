package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.textfield.GrayBackgroundTextField
import com.familring.presentation.screen.timecapsule.WritingState.FINISHED_TIME_CAPSULE
import com.familring.presentation.screen.timecapsule.WritingState.NO_TIME_CAPSULE
import com.familring.presentation.screen.timecapsule.WritingState.WRITING_TIME_CAPSULE
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

object WritingState {
    const val NO_TIME_CAPSULE = 0
    const val FINISHED_TIME_CAPSULE = 1
    const val WRITING_TIME_CAPSULE = 2
}

@Composable
fun WritingTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    navigateToCreate: () -> Unit = {},
    state: TimeCapsuleUiState,
    getTimeCapsuleStatus: () -> Unit = {},
    createTimeCapsuleAnswer: (String) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        getTimeCapsuleStatus()
    }

    when (state.writingStatus) {
        NO_TIME_CAPSULE ->
            NoTimeCapsule(
                modifier = modifier,
                navigateToCreate = navigateToCreate,
            )

        FINISHED_TIME_CAPSULE ->
            FinishedTimeCapsule(
                modifier = modifier,
                state = state,
            )

        WRITING_TIME_CAPSULE ->
            WritingTimeCapsule(
                modifier = modifier,
                state = state,
                createTimeCapsuleAnswer = createTimeCapsuleAnswer,
            )
    }
}

@Composable
fun WritingTimeCapsule(
    modifier: Modifier = Modifier,
    state: TimeCapsuleUiState,
    createTimeCapsuleAnswer: (String) -> Unit = {},
) {
    val textFieldState = remember { TextFieldState("") }
    val contentScrollState = rememberScrollState()

    // 키보드 높이 감지
    val imeInsets = WindowInsets.ime.exclude(WindowInsets.navigationBars)
    val imeHeight = imeInsets.getBottom(LocalDensity.current)

    val scrollState = rememberScrollState()

    LaunchedEffect(imeHeight) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        color = White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        bottom =
                            with(LocalDensity.current) {
                                if ((imeHeight).toDp() >= 30.dp) {
                                    (imeHeight).toDp() - 30.dp
                                } else {
                                    0.dp
                                }
                            },
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.img_pill),
                contentDescription = "pill",
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${state.timeCapsuleCount}번째 타임캡슐을 작성해 보세요!",
                style = Typography.headlineLarge.copy(fontSize = 26.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "미래 속 가족에게 하고 싶은 말이 있나요?",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = Modifier.height(20.dp))
            GrayBackgroundTextField(
                textFieldState = textFieldState,
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(3.3f / 2f)
                        .fillMaxHeight(0.5f),
                scrollState = contentScrollState,
            )
            Spacer(modifier = Modifier.height(10.dp))
            RoundLongButton(
                text = "작성 완료",
                onClick = { createTimeCapsuleAnswer(textFieldState.text.toString()) },
                enabled = textFieldState.text.toString().isNotBlank(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f),
                text = "작성 완료한 가족",
                style =
                    Typography.displaySmall.copy(
                        fontSize = 18.sp,
                        color = Gray01,
                    ),
            )
            Spacer(modifier = Modifier.height(3.dp))
            if (state.writers.isEmpty()) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 5.dp, bottom = 5.dp),
                    text = "아직 작성을 완료한 가족이 없어요!",
                    style =
                        Typography.labelSmall.copy(
                            fontSize = 16.sp,
                            color = Gray01,
                        ),
                )
            } else {
                OverlappingProfileLazyRow(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.9f)
                            .padding(bottom = 5.dp),
                    profiles = state.writers,
                )
            }
        }
    }
}

@Preview
@Composable
private fun WritingTimeCapsulePreview() {
    WritingTimeCapsule(
        state = TimeCapsuleUiState(),
    )
}
