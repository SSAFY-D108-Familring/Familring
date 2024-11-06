package com.familring.presentation.screen.question

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun AnswerWriteRoute(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    showSnackBar: (String) -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    val questionEvent by viewModel.questionEvent.collectAsStateWithLifecycle(initialValue = QuestionEvent.Loading)

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

    AnswerWriteScreen(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        onSubmit = { content -> viewModel.postAnswer(content) },
    )
}

@Composable
fun AnswerWriteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    var content by remember { mutableStateOf("") }

    Surface(modifier = modifier.fillMaxSize(),
        color = White) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
        ) {
            TopAppBar(
                title = { Text(text = "답변 작성", style = Typography.headlineMedium) },
                onNavigationClick = onNavigateBack,
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                placeholder = { Text("답변을 작성해주세요") },
                textStyle = Typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "작성하기",
                style = Typography.titleLarge,
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .clickable(enabled = content.isNotEmpty()) {
                            onSubmit(content)
                        },
                color = if (content.isNotEmpty()) Green01 else Gray02,
            )

            Spacer(modifier = Modifier.height(32.dp))
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
