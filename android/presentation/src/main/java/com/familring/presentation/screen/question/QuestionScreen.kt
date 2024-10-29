package com.familring.presentation.screen.question

import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.familring.domain.Profile
import com.familring.domain.UserQuestion
import com.familring.presentation.R
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.FamilringTheme
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

@Composable
fun QuestionScreen(navigateToQuestionList: () -> Unit) {
    var family = remember { mutableListOf<String>() }
    val userQuestion =
        UserQuestion(
            profile = Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
            question = null,
        )

    val questionList =
        listOf(
            UserQuestion(
                profile = Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                question = null,
            ),
            UserQuestion(
                profile = Profile(zodiacImgUrl = "url2", backgroundColor = "0xFFFEE222"),
                question = "답변1",
            ),
            UserQuestion(
                profile = Profile(zodiacImgUrl = "url3", backgroundColor = "0xFFFEE222"),
                question = null,
            ),
            UserQuestion(
                profile = Profile(zodiacImgUrl = "url4", backgroundColor = "0xFFFEE222"),
                question = "답변2",
            ),
            UserQuestion(
                profile = Profile(zodiacImgUrl = "url5", backgroundColor = "0xFFFEE222"),
                question = null,
            ),
        )

    Box(
        modifier = Modifier.fillMaxSize(),
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
            Spacer(modifier = Modifier.fillMaxSize(0.02f))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "오늘의 질문",
                    style = Typography.titleLarge,
                )
                Image(
                    painter = painterResource(id = R.drawable.img_menu),
                    contentDescription = "question_menu_img",
                    modifier =
                        Modifier.size(24.dp).noRippleClickable {
                            navigateToQuestionList()
                        },
                )
            }

            Spacer(modifier = Modifier.fillMaxSize(0.03f))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_pin_title_question),
                    contentDescription = "pin_title_question_img",
                    contentScale = ContentScale.Fit,
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "99번째 질문",
                        textAlign = TextAlign.Center,
                        style = Typography.bodySmall,
                        modifier =
                            Modifier
                                .background(color = Green01, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 15.dp, vertical = 8.dp),
                    )
                    Spacer(modifier = Modifier.fillMaxSize(0.03f))
                    Text(
                        text = "그동안 미안했지만 사과하지 못했던 일이 있나요? 있다면 여기에 적어보세요",
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        modifier = Modifier.padding(horizontal = 50.dp),
                        overflow = TextOverflow.Visible,
                        style = Typography.displayMedium.copy(fontSize = 22.sp),
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
                items(questionList) { question ->
                    FamilyListItem(userQuestion = question)
                }
            }
        }
    }
}

@Composable
fun FamilyListItem(userQuestion: UserQuestion) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            ZodiacBackgroundProfile(
                profile = Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                modifier =
                    Modifier
                        .size(35.dp)
                        .fillMaxSize(),
            )
            Spacer(modifier = Modifier.fillMaxSize(0.03f))
            Text(
                text = "엄마미의 답변",
                style = Typography.headlineSmall.copy(fontSize = 17.sp),
                color = Gray01,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (userQuestion.question != null) {
            Text(
                text = "나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아 나경아",
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
                            Log.d(
                                "question",
                                "똑똑 누름 $userQuestion",
                            )
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    FamilringTheme {
//        QuestionScreen(onNavigateToQuestionList = {})
    }
}
