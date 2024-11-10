package com.familring.presentation.screen.question

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.textfield.NoBorderTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green01
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
                    questionId = state.questionId,
                    questionContent = state.questionContent,
                    initialAnswer = state.answerContents[0].answerContent,
                    onSubmit = { content -> viewModel.patchAnswer(content) },
                )
            } else {
                Timber.d("답변 없음")
                AnswerWriteScreen(
                    modifier = modifier,
                    onNavigateBack = onNavigateBack,
                    questionId = state.questionId,
                    questionContent = state.questionContent,
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
    questionId: Long = 0,
    questionContent: String = "",
    initialAnswer: String = "",
    onSubmit: (String) -> Unit,
) {
    var content by remember { mutableStateOf(initialAnswer) }
    val focusManager = LocalFocusManager.current
    val maxLength = 100
    val isEnabled = content.isNotEmpty()

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
            Column {
                Spacer(modifier = Modifier.fillMaxSize(0.012f))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .shadow(
                                elevation = 9.dp,
                                spotColor = Color.Black.copy(alpha = 0.6f),
                                ambientColor = Color.Black.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp),
                            ).background(
                                Color.White,
                                shape = RoundedCornerShape(10.dp),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.fillMaxSize(0.03f))
                        Text(
                            text = "${questionId}번째 질문",
                            textAlign = TextAlign.Center,
                            style = Typography.bodySmall,
                            color = White,
                            modifier =
                                Modifier
                                    .background(
                                        color = Green02,
                                        shape = RoundedCornerShape(8.dp),
                                    ).padding(horizontal = 15.dp, vertical = 8.dp),
                        )
                        Spacer(modifier = Modifier.fillMaxSize(0.03f))
                        Text(
                            text = questionContent,
                            textAlign = TextAlign.Center,
                            softWrap = true,
                            modifier = Modifier.padding(horizontal = 26.dp),
                            overflow = TextOverflow.Visible,
                            style = Typography.displayMedium.copy(fontSize = 22.sp),
                            color = Black,
                        )
                        Spacer(modifier = Modifier.fillMaxSize(0.05f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            NoBorderTextField(
                modifier = Modifier.padding(horizontal = 10.dp),
                value = content,
                onValueChange = {
                    if (it.length <= maxLength){
                        content = it
                    }
                },
                placeholder = "답변을 입력해 주세요",
                focusManager = focusManager,
            )
            Text(
                text = "${content.length}/${maxLength}",
                style = Typography.bodySmall,
                color = Gray02,
                modifier = Modifier.align(Alignment.End).padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text( // 버튼이 왜 안눌리지  ㅠ
                text = if (initialAnswer.isEmpty()) "답변 저장하기" else "답변 수정하기",
                modifier = Modifier.noRippleClickable {
                    if (isEnabled) onSubmit(content)
                },
            )
            Spacer(modifier = Modifier.fillMaxSize(0.06f))
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
