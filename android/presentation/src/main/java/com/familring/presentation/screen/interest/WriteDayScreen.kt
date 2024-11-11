package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.textfield.GrayBigTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

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
    var interestKeyword by remember { mutableStateOf(interest) }
    var canEdit by remember { mutableStateOf(!isWroteInterest) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(canEdit) {
        if (canEdit) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
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
            GrayBigTextField(
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester),
                keyword = interestKeyword,
                onValueChange = {
                    interestKeyword = it
                },
                placeHolder = stringResource(R.string.interest_keyword_placeholder),
                focusManager = focusManager,
                maxLength = 10,
                enabled = canEdit,
                onDone = {
                    if (interestKeyword.isNotEmpty()) {
                        if (isWroteInterest) {
                            editInterest(interestKeyword) // 관심사 수정
                        } else {
                            writeInterest(interestKeyword)
                        }
                        canEdit = false
                    }
                },
            )
            if (!isWroteInterest) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.interest_ten_word),
                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                    color = Gray02,
                )
            } else {
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Text(
                    modifier =
                        Modifier.noRippleClickable {
                            canEdit = true
                        },
                    text = stringResource(R.string.interest_edit_btn),
                    style = Typography.displayMedium.copy(fontSize = 18.sp),
                    color = Gray02,
                    textDecoration = TextDecoration.Underline,
                )
            }
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
