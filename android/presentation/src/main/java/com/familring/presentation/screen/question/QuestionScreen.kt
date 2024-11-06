package com.familring.presentation.screen.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.Profile
import com.familring.domain.model.QuestionAnswer
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import timber.log.Timber

@Composable
fun QuestionRoute(
    modifier: Modifier,
    navigateToQuestionList: () -> Unit,
    showSnackBar: (String) -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    val questionState by viewModel.questionState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (val state = questionState) {
        is QuestionState.Loading -> {
            Timber.tag("nakyung").d("질문화면 로딩중")
        }

        is QuestionState.Success -> {
            QuestionScreen(
                modifier = modifier,
                navigateToQuestionList = navigateToQuestionList,
                showSnackBar = showSnackBar,
                questionId = state.questionId,
                questionContent = state.questionContent,
                answerContents = state.answerContents,
            )
        }

        is QuestionState.Error -> {
            QuestionScreen(
                modifier = modifier,
                navigateToQuestionList = navigateToQuestionList,
                showSnackBar = showSnackBar,
            )
        }
    }
}

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    navigateToQuestionList: () -> Unit,
    showSnackBar: (String) -> Unit = {},
    questionId: Long = 0,
    questionContent: String = "",
    answerContents: List<QuestionAnswer> = listOf(),
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(bottom = 60.dp),
                ) {
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Surface(
                                    color = Gray01.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(end = 8.dp),
                                ) {
                                    Text(
                                        text = "답변 수정하기",
                                        style = Typography.labelSmall,
                                        color = White,
                                        modifier =
                                            Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp,
                                            ),
                                    )
                                }
                                FloatingActionButton(
                                    onClick = { /*수정하기*/ },
                                    shape = RoundedCornerShape(50.dp),
                                    modifier =
                                        Modifier
                                            .padding(end = 7.dp)
                                            .size(40.dp),
                                    containerColor = Green01,
                                    elevation =
                                        FloatingActionButtonDefaults.elevation(
                                            defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp,
                                        ),
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_add),
                                        contentDescription = "ic_add",
                                        tint = White,
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Surface(
                                    color = Gray01.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(end = 8.dp),
                                ) {
                                    Text(
                                        "답변 작성하기",
                                        style = Typography.labelSmall,
                                        color = Color.White,
                                        modifier =
                                            Modifier.padding(
                                                vertical = 8.dp,
                                                horizontal = 16.dp,
                                            ),
                                    )
                                }
                                FloatingActionButton(
                                    onClick = { /*수정하기*/ },
                                    shape = RoundedCornerShape(50.dp),
                                    modifier =
                                        Modifier
                                            .padding(end = 7.dp)
                                            .size(40.dp),
                                    containerColor = Green01,
                                    elevation =
                                        FloatingActionButtonDefaults.elevation(
                                            defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp,
                                        ),
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_add),
                                        contentDescription = "ic_add",
                                        tint = White,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxSize(0.02f))
                    FloatingActionButton(
                        onClick = {
                            isExpanded = !isExpanded
                        },
                        shape = RoundedCornerShape(50.dp),
                        containerColor = Green01,
                        modifier = Modifier.size(56.dp),
                        elevation =
                            FloatingActionButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp,
                            ),
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = "fab_img",
                            tint = White,
                        )
                    }
                }
            },
        ) { paddingValues ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = White)
                        .padding(paddingValues),
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
                        navigationType = TopAppBarNavigationType.None,
                        title = {
                            Text(
                                text = "오늘의 질문",
                                style = Typography.titleLarge,
                            )
                        },
                        trailingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.img_menu),
                                contentDescription = "question_menu_img",
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .noRippleClickable {
                                            navigateToQuestionList()
                                        },
                            )
                        },
                    )
                    Spacer(modifier = Modifier.fillMaxSize(0.03f))
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
                                        modifier =
                                            Modifier
                                                .background(
                                                    color = Green01,
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
                                    )
                                    Spacer(modifier = Modifier.fillMaxSize(0.05f))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 46.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_pin),
                                contentDescription = "pin img",
                                contentScale = ContentScale.Fit,
                            )
                            Image(
                                painter = painterResource(id = R.drawable.img_pin),
                                contentDescription = "pin img",
                                contentScale = ContentScale.Fit,
                            )
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
                        items(answerContents.size) { answer ->
                            FamilyListItem(answerContents[answer])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyListItem(questionAnswer: QuestionAnswer) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            ZodiacBackgroundProfile(
                profile = Profile(zodiacImgUrl = questionAnswer.userZodiacSign, backgroundColor = questionAnswer.userColor),
                modifier =
                    Modifier
                        .size(35.dp)
                        .fillMaxSize(),
            )
            Spacer(modifier = Modifier.fillMaxSize(0.03f))
            Text(
                text = "${questionAnswer.userNickname}의 답변",
                style = Typography.headlineSmall.copy(fontSize = 17.sp),
                color = Gray01,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (questionAnswer.answerStatus) {
            Text(
                text = questionAnswer.answerContent,
                style = Typography.displaySmall.copy(fontSize = 18.sp),
            )
        } else {
            Row {
                Text(
                    text = "아직 답변을 작성하지 않았어요!  ",
                    style = Typography.displaySmall.copy(fontSize = 18.sp),
                    color = Gray02,
                )
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(
                                style =
                                    SpanStyle(
                                        textDecoration = TextDecoration.Underline,
                                    ),
                            ) {
                                append("✊\uD83C\uDFFB 똑똑")
                            }
                        },
                    style = Typography.headlineSmall.copy(fontSize = 18.sp),
                    modifier =
                        Modifier.noRippleClickable {
                            Timber.d("똑똑 누름 " + questionAnswer.userId)
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        navigateToQuestionList = {},
        showSnackBar = {},
    )
}
