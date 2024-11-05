package com.familring.presentation.screen.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.BrownRoundButton
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DoneRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    navigateToHome: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.makeFamily()
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.Loading -> {
                    // 로딩창 띄우기
                }

                is SignUpUiEvent.MakeSuccess -> {
                    showSnackBar("코드를 이용해 가족을 초대해 보세요!")
                }

                else -> {
                    // 에러 스낵바 띄우기
                }
            }
        }
    }

    DoneScreen(
        modifier = modifier,
        navigateToHome = navigateToHome,
        code = uiState.familyCode,
    )
}

@Composable
fun DoneScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    code: String = "",
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Text(
                text = "완료!",
                style = Typography.titleLarge.copy(fontSize = 45.sp),
                color = Green03,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.45f)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_mailbox),
                contentDescription = "img_family",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Text(
                text = "코드를 공유하고",
                style = Typography.displayLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Text(
                text = "가족을 초대해 보세요!",
                style = Typography.displayLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(
                modifier =
                    Modifier.run {
                        fillMaxWidth(0.82f)
                            .wrapContentSize()
                            .background(color = White)
                            .border(width = 2.dp, color = Gray03, shape = RoundedCornerShape(15.dp))
                    },
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 20.dp, horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "나의 초대코드",
                        color = Gray02,
                        style = Typography.displayMedium.copy(fontSize = 18.sp),
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = code,
                        style =
                            Typography.headlineMedium.copy(
                                fontSize = 28.sp,
                                letterSpacing = 0.em,
                            ),
                        color = Black,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    BrownRoundButton(onClick = { /*TODO*/ }, text = "카카오톡으로 공유하기")
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.35f))
            RoundLongButton(
                text = "홈으로 이동하기",
                onClick = navigateToHome,
            )
        }
    }
}

@Preview
@Composable
fun DoneScreenPreview() {
    DoneScreen()
}
