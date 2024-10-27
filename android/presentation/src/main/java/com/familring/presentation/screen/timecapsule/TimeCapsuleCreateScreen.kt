package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.GreenRoundLongButton
import com.familring.presentation.component.NumberTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography

@Composable
fun TimeCapsuleCreateScreen(modifier: Modifier = Modifier) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val isButtonEnabled = isDateFormValid(year, month, date)

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                year = year,
                month = month,
                date = date,
                onYearChange = { year = it },
                onMonthChange = { month = it },
                onDateChange = { date = it },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            GreenRoundLongButton(
                text = "타임캡슐 작성하러 가기",
                onClick = { /*TODO*/ },
                enabled = isButtonEnabled,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }
}

@Composable
private fun DateInputRow(
    year: String,
    month: String,
    date: String,
    onYearChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.07f))
        NumberTextField(
            modifier = Modifier.weight(1.5f),
            number = year,
            onValueChange = onYearChange,
            placeholder = "YYYY",
            focusManager = focusManager,
            maxLength = 4,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = month,
            onValueChange = onMonthChange,
            placeholder = "MM",
            focusManager = focusManager,
            maxLength = 2,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = date,
            onValueChange = onDateChange,
            placeholder = "DD",
            focusManager = focusManager,
            maxLength = 2,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.07f))
    }
}

private fun isDateFormValid(
    year: String,
    month: String,
    date: String,
): Boolean = year.length == 4 && (month.length in 1..2) && (date.length in 1..2)

@Preview
@Composable
private fun TimeCapsuleCreateScreenPreview() {
    TimeCapsuleCreateScreen()
}
