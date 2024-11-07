package com.familring.presentation.screen.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familring.presentation.R
import com.familring.presentation.component.button.BrownRoundButton
import com.familring.presentation.component.textfield.GrayBigTextField
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FirstRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToBirth: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.NotAvailable -> showSnackBar("존재하지 않는 코드입니다")
                is SignUpUiEvent.Available -> {
                    viewModel.updateMake(false)
                    navigateToBirth()
                }

                else -> {}
            }
        }
    }

    FirstScreen(
        modifier = modifier,
        navigateToBirth = navigateToBirth,
        updateCode = viewModel::updateCode,
        updateMake = viewModel::updateMake,
        checkAvailable = viewModel::isAvailableCode,
    )
}

@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    navigateToBirth: () -> Unit = {},
    updateCode: (String) -> Unit = {},
    updateMake: (Boolean) -> Unit = {},
    checkAvailable: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var code by remember { mutableStateOf("") }

    LaunchedEffect(code) {
        updateCode(code)
    }

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
                painter = painterResource(id = R.drawable.img_signup_emoji),
                contentDescription = "signup_emoji",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "패밀링에 오신 것을 환영해요!",
                style = Typography.titleLarge.copy(fontSize = 28.sp),
                color = Green03,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "받으신 초대코드가 있으신가요?",
                style = Typography.bodyLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.08f))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.82f)
                        .wrapContentSize()
                        .background(color = White)
                        .border(width = 2.dp, color = Gray03, shape = RoundedCornerShape(15.dp)),
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
                        text = "초대코드 입력",
                        color = Gray02,
                        style = Typography.displayMedium.copy(fontSize = 18.sp),
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    GrayBigTextField(
                        keyword = code,
                        onValueChange = {
                            code = it
                        },
                        focusManager = focusManager,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    BrownRoundButton(
                        onClick = {
                            checkAvailable(code)
                        },
                        text = "코드 입력 완료",
                        enabled = code.length == 6,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                modifier =
                    Modifier.noRippleClickable {
                        updateMake(true)
                        navigateToBirth()
                    },
                text = "없어요, 새로 개설할래요!",
                style = Typography.headlineSmall,
                color = Gray01,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpContentPreview() {
    FirstScreen(modifier = Modifier, navigateToBirth = {})
}
