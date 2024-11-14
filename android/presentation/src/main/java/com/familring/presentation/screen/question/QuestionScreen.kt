package com.familring.presentation.screen.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.familring.domain.model.question.QuestionAnswer
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.TutorialScreen
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionRoute(
    modifier: Modifier,
    navigateToQuestionList: () -> Unit,
    navigateToAnswerWrite: () -> Unit,
    showSnackBar: (String) -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    val tutorialUiState by viewModel.tutorialUiState.collectAsStateWithLifecycle()

    val questionState by viewModel.questionState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTutorial by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (val state = questionState) {
        is QuestionState.Loading -> {
            Timber.tag("nakyung").d("질문화면 로딩중")
            isLoading = true
        }

        is QuestionState.Success -> {
            isLoading = false
            QuestionScreen(
                modifier = modifier,
                navigateToQuestionList = navigateToQuestionList,
                navigateToAnswerWrite = navigateToAnswerWrite,
                showSnackBar = showSnackBar,
                showTutorial = {
                    showTutorial = true
                    viewModel.setReadTutorialState(false)
                },
                questionId = state.questionId,
                questionContent = state.questionContent,
                answerContents = state.answerContents,
            )

            if (showTutorial && !tutorialUiState.isReadTutorial) {
                ModalBottomSheet(
                    containerColor = White,
                    onDismissRequest = {
                        showTutorial = false
                        viewModel.setReadTutorial()
                    },
                    sheetState = sheetState,
                ) {
                    TutorialScreen(
                        imageLists =
                            listOf(
                                R.drawable.img_tutorial_question_first,
                                R.drawable.img_tutorial_question_second,
                                R.drawable.img_tutorial_question_third,
                                R.drawable.img_tutorial_question_fourth,
                            ),
                        title = "랜덤 질문 미리보기 \uD83D\uDD0D",
                        subTitle =
                            "가족 모두가 참여하면 아침 9시에\n" +
                                "랜덤으로 새로운 질문이 생성되요!",
                    )
                }
            }
        }

        is QuestionState.Error -> {
            isLoading = false
            QuestionScreen(
                modifier = modifier,
                navigateToQuestionList = navigateToQuestionList,
                navigateToAnswerWrite = navigateToAnswerWrite,
                showSnackBar = showSnackBar,
            )
        }
    }

    if (isLoading) {
        LoadingDialog(loadingMessage = "질문 로딩중...")
    }
}

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    navigateToQuestionList: () -> Unit,
    navigateToAnswerWrite: () -> Unit,
    showSnackBar: (String) -> Unit = {},
    showTutorial: () -> Unit = {},
    questionId: Long = 0,
    questionContent: String = "",
    answerContents: List<QuestionAnswer> = listOf(),
) {
    Surface(modifier = modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Spacer(modifier = Modifier.fillMaxSize(0.02f))
                    FloatingActionButton(
                        onClick = {
                            navigateToAnswerWrite()
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
                            imageVector =
                                if (answerContents[0].answerStatus) {
                                    Icons.Default.Edit
                                } else {
                                    Icons.Default.Add
                                },
                            contentDescription = if (answerContents[0].answerStatus) "edit_answer" else "write_answer",
                            tint = White,
                        )
                    }
                }
            },
        ) { _ ->
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
                        navigationType = TopAppBarNavigationType.None,
                        title = {
                            Text(
                                text = "오늘의 질문",
                                style = Typography.titleLarge,
                                color = Black,
                            )
                        },
                        tutorialIcon = {
                            Icon(
                                modifier =
                                    Modifier
                                        .size(20.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Gray03,
                                            shape = CircleShape,
                                        ).padding(2.dp)
                                        .noRippleClickable { showTutorial() },
                                painter = painterResource(id = R.drawable.ic_tutorial),
                                contentDescription = "ic_question",
                                tint = Gray03,
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

                    Spacer(modifier = Modifier.fillMaxSize(0.05f))
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        items(answerContents.size) { answer ->
                            FamilyListItem(
                                answerContents[answer],
                                showSnackBar,
                                answer,
                                questionId = questionId,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyListItem(
    questionAnswer: QuestionAnswer,
    showSnackBar: (String) -> Unit,
    answer: Int,
    viewModel: QuestionViewModel = hiltViewModel(),
    questionId: Long,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            ZodiacBackgroundProfile(
                profile =
                    Profile(
                        zodiacImgUrl = questionAnswer.userZodiacSign,
                        backgroundColor = questionAnswer.userColor,
                    ),
                paddingValue = 4,
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
                color = Black,
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
                color = Gray02,
                modifier =
                    Modifier.noRippleClickable {
                        viewModel.knockKnock(
                            questionId,
                            questionAnswer.userId,
                        )
                        Timber.d("똑똑 누름 " + questionAnswer.userId)
                        showSnackBar("${questionAnswer.userNickname}을/를 똑똑 두드렸어요~ ㅋㅋ")
                    },
            )
        } else {
            Row {
                Text(
                    text = "아직 답변을 작성하지 않았어요!  ",
                    style = Typography.displaySmall.copy(fontSize = 18.sp),
                    color = Gray02,
                )
//                if (answer != 0) {
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
                    color = Gray02,
                    modifier =
                        Modifier.noRippleClickable {
                            viewModel.knockKnock(
                                questionId,
                                questionAnswer.userId,
                            )
                            Timber.d("똑똑 누름 " + questionAnswer.userId)
                            showSnackBar("${questionAnswer.userNickname}을/를 똑똑 두드렸어요~ ㅋㅋ")
                        },
                )
//                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        navigateToQuestionList = {},
        navigateToAnswerWrite = {},
        showSnackBar = {},
    )
}
