package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.apache.commons.lang3.StringUtils.isNotBlank

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
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                modifier = Modifier.size(130.dp),
                painter = painterResource(id = R.drawable.img_pill),
                contentDescription = "pill",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "${state.timeCapsuleCount}번째 타임캡슐을 작성해 보세요!",
                style = Typography.headlineLarge.copy(fontSize = 26.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "미래 속 가족에게 하고 싶은 말이 있나요?",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            GrayBackgroundTextField(
                textFieldState = textFieldState,
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.5f),
                scrollState = scrollState,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            RoundLongButton(
                text = "작성 완료",
                onClick = { createTimeCapsuleAnswer(textFieldState.text.toString()) },
                enabled = textFieldState.text.toString().isNotBlank(),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.06f))
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
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            if (state.writers.isEmpty()) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 5.dp),
                    text = "아직 작성을 완료한 가족이 없어요!",
                    style =
                        Typography.labelSmall.copy(
                            fontSize = 16.sp,
                            color = Gray01,
                        ),
                )
            } else {
                OverlappingProfileLazyRow(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    profiles = state.writers,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
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
