package com.familring.presentation.screen.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun PastQuestionRoute(
    questionId: Long,
    popUpBackStack: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    // questionId를 1로 하드코딩하지 말고 파라미터로 받은 값을 사용
    PastQuestionScreen(
        questionId = questionId, // 여기를 수정
        popUpBackStack = popUpBackStack,
        viewModel = viewModel
    )
}

@Composable
fun PastQuestionScreen(
    questionId: Long,
    popUpBackStack: () -> Unit,
    viewModel: QuestionViewModel,
) {
    val questionState by viewModel.questionState.collectAsStateWithLifecycle()

    LaunchedEffect(questionId) {
        viewModel.getQuestion(questionId)
    }

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
            Surface(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Column(modifier = Modifier.fillMaxSize()) {
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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            TopAppBar(
                                title = {},
                                onNavigationClick = popUpBackStack,
                            )
                            Box {
                                Column {
                                    Spacer(modifier = Modifier.fillMaxSize(0.012f))
                                    Box(
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                            .shadow(
                                                elevation = 6.dp,
                                                spotColor = Color.Black.copy(alpha = 0.6f),
                                                ambientColor = Color.Black.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(10.dp),
                                            )
                                            .background(
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
                                                text = "${state.questionId}번째 질문",
                                                textAlign = TextAlign.Center,
                                                style = Typography.bodySmall,
                                                modifier =
                                                Modifier
                                                    .background(
                                                        color = Green01,
                                                        shape = RoundedCornerShape(8.dp),
                                                    )
                                                    .padding(horizontal = 15.dp, vertical = 8.dp),
                                            )
                                            Spacer(modifier = Modifier.fillMaxSize(0.03f))
                                            Text(
                                                text = state.questionContent,
                                                textAlign = TextAlign.Center,
                                                softWrap = true,
                                                modifier = Modifier.padding(horizontal = 26.dp),
                                                overflow = TextOverflow.Visible,
                                                style = Typography.displayMedium.copy(fontSize = 22.sp),
                                            )
                                            Spacer(modifier = Modifier.fillMaxSize(0.05f))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.fillMaxSize(0.05f))
                            LazyColumn(
                                modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                            ) {
                                items(state.answerContents.size) { index ->
                                    FamilyListItem(state.answerContents[index], {})
                                }
                            }
                        }
                    }
                }
            }
        }

        is QuestionState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.errorMessage,
                    style = Typography.bodyLarge,
                    color = Color.Red,
                )
            }
        }
    }
}
