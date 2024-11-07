package com.familring.presentation.screen.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.textfield.CustomTextField
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun EditNameRoute(
    modifier: Modifier,
    viewModel: MyPageViewModel,
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    EditNameScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        showSnackBar = showSnackBar,
        updateNickname = viewModel::updateName,
    )
}

@Composable
fun EditNameScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    updateNickname: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var nickname by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "닉네임 변경",
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                        color = Black,
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "변경하고 싶은 닉네임을 입력해 주세요",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.08f))
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = nickname,
                    onValueChanged = {
                        nickname = it
                    },
                    placeHolder = "닉네임을 입력해 주세요",
                    focusManager = focusManager,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            RoundLongButton(
                text = "변경하기",
                onClick = { updateNickname(nickname) },
                enabled = nickname.isNotEmpty(),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EditNamePreview() {
    EditNameScreen()
}
