package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.textfield.GrayBigTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun WriteDayScreen(
    modifier: Modifier = Modifier,
    isWroteInterest: Boolean = false,
    interest: String = "",
    isFamilyWrote: Boolean = false,
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
) {
    var interestKeyword by remember(interest) { mutableStateOf(interest) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        // 키보드 내리기
                        focusManager.clearFocus()
                    })
                },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.interest_script_1),
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = stringResource(R.string.interest_script_recent),
                style = Typography.titleSmall.copy(fontSize = 22.sp),
                color = Green03,
            )
        }
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = stringResource(R.string.interest_script_2),
            style = Typography.bodyLarge.copy(fontSize = 22.sp),
            color = Black,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.45f)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_interest_heart),
                contentDescription = "img_interest_heart",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = stringResource(R.string.my_recent_interest),
                style = Typography.displayMedium.copy(fontSize = 22.sp),
                color = Gray02,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .height(IntrinsicSize.Max),
            ) {
                GrayBigTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.72f)
                            .focusRequester(focusRequester),
                    keyword = interestKeyword,
                    onValueChange = {
                        interestKeyword = it
                    },
                    placeHolder = stringResource(R.string.interest_keyword_placeholder),
                    focusManager = focusManager,
                    maxLength = 10,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    onClick = {
                        if (!isWroteInterest) {
                            writeInterest(interestKeyword)
                        } else {
                            editInterest(interestKeyword)
                        }
                        focusManager.clearFocus()
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Green02,
                            contentColor = White,
                            disabledContainerColor = Gray03,
                            disabledContentColor = White,
                        ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = interestKeyword.isNotEmpty() and interestKeyword.isNotBlank(),
                ) {
                    Text(
                        text = if (!isWroteInterest) "등록" else "수정",
                        style =
                            Typography.headlineSmall.copy(
                                fontSize = 20.sp,
                            ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.interest_ten_word),
                style = Typography.displayMedium.copy(fontSize = 16.sp),
                color = Gray02,
            )
            Spacer(modifier = Modifier.weight(1f))
            RoundLongButton(
                text = "다른 가족이 작성한 관심사 확인하기",
                onClick = navigateToOtherInterest,
                enabled = isFamilyWrote,
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WriteDayScreenPreview() {
    WriteDayScreen()
}
