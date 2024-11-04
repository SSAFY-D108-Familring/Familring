package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familring.presentation.R
import com.familring.presentation.component.DateInputRow
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.isDateFormValid

@Composable
fun TimeCapsuleCreateRoute(
    modifier: Modifier = Modifier,
    timeCapsuleCreateViewModel: TimeCapsuleCreateViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
) {
    TimeCapsuleCreateScreen(
        modifier = modifier,
        createTimeCapsule = timeCapsuleCreateViewModel::createTimeCapsule,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun TimeCapsuleCreateScreen(
    modifier: Modifier = Modifier,
    createTimeCapsule: (String) -> Unit = {},
    popUpBackStack: () -> Unit = {},
) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val isButtonEnabled = isDateFormValid(year, month, date)

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "타임 캡슐 작성",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.img_wrapped_gift),
                contentDescription = "wrapped_gift",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "타임캡슐 오픈일을 지정해 주세요!",
                style = Typography.headlineLarge.copy(fontSize = 26.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "미래의 가족이 열어볼 타임캡슐을 준비해 보세요",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            DateInputRow(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally),
                year = year,
                month = month,
                date = date,
                onYearChange = { year = it },
                onMonthChange = { month = it },
                onDateChange = { date = it },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            RoundLongButton(
                text = "타임캡슐 작성하러 가기",
                onClick = { createTimeCapsule("$year-$month-$date") },
                enabled = isButtonEnabled,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleCreateScreenPreview() {
    TimeCapsuleCreateScreen()
}
