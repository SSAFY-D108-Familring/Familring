package com.familring.presentation.screen.question

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.textfield.NoBorderTextField
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import timber.log.Timber

@Composable
fun AnswerWriteRoute(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    showSnackBar: (String) -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    val questionEvent by viewModel.questionEvent.collectAsStateWithLifecycle(initialValue = QuestionEvent.Loading)
    val questionState by viewModel.questionState.collectAsStateWithLifecycle()
    when (val state = questionState) {
        is QuestionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Green02)
            }
        }

        is QuestionState.Success -> {
            if (state.answerContents[0].answerStatus) {
                Timber.d("답변 잇음")
                AnswerWriteScreen(
                    modifier = modifier,
                    onNavigateBack = onNavigateBack,
                    onSubmit = { content -> viewModel.patchAnswer(content) },
                )
            } else {
                Timber.d("답변 없음")
                AnswerWriteScreen(
                    modifier = modifier,
                    onNavigateBack = onNavigateBack,
                    onSubmit = { content -> viewModel.postAnswer(content) },
                )
            }
        }

        is QuestionState.Error -> {
            showSnackBar("에러가 발생했씁니다..")
        }
    }

    LaunchedEffect(questionEvent) {
        when (val event = questionEvent) {
            is QuestionEvent.Loading -> {
                // 답변 대기중...
            }

            is QuestionEvent.Success -> {
                showSnackBar("답변이 작성되었습니다")
                onNavigateBack()
            }

            is QuestionEvent.Error -> {
                showSnackBar(event.message)
            }

            null -> {
                showSnackBar("답변을 작성해주세요")
            }
        }
    }
}

@Composable
fun AnswerWriteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = { Text(text = "답변 작성", style = Typography.headlineMedium) },
                onNavigationClick = onNavigateBack,
            )

            Spacer(modifier = Modifier.height(20.dp))
            NoBorderTextField(
                value = content,
                onValueChange = {
                    content = it
                },
                placeholder = "답변을 입력해 주세요",
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                modifier =
                    Modifier
                        .clickable(enabled = content.isNotEmpty()) { }
                        .noRippleClickable { onSubmit(content) },
                text = "답변 남기기",
                style = Typography.titleSmall,
                color = if (content.isNotEmpty()) Green02 else Gray03,
                textDecoration = TextDecoration.Underline,
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun AnswerWriteScreenPreview() {
    AnswerWriteScreen(
        onNavigateBack = {},
        onSubmit = {},
    )
}
