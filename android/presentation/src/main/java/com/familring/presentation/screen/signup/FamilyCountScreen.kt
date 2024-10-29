package com.familring.presentation.screen.signup

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.NoBorderTextField
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography

@Composable
fun FamilyCountRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToDone: () -> Unit,
) {
    FamilyCountScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToDone = navigateToDone,
    )
}

@Composable
fun FamilyCountScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToDone: () -> Unit = {},
) {
    var count by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "가족 구성원 수 입력",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                text = "참여하는 가족 구성원 수를 알려 주세요!",
                style = Typography.bodyLarge,
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.45f)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_family),
                contentDescription = "img_family",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "우리 가족은",
                    style = Typography.headlineLarge,
                    color = Gray01,
                )
                NoBorderTextField(
                    value = count,
                    onValueChange = {
                        count = it
                    },
                    placeholder = "0",
                    focusManager = focusManager,
                )
                Text(
                    text = "명이에요",
                    style = Typography.headlineLarge,
                    color = Gray01,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            RoundLongButton(
                onClick = navigateToDone,
                text = "다음으로",
                enabled = count.isNotEmpty(),
            )
        }
    }
}

@Preview
@Composable
fun FamilyCountScreenPreview() {
    FamilyCountScreen()
}
