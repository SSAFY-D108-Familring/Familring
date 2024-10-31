package com.familring.presentation.screen.calendar

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.familring.presentation.component.DateInputRow
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TimeInputRow
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun TimeSelectTap(modifier: Modifier = Modifier) {
    var isStartSelected by remember { mutableStateOf(true) }

    var startYear by remember { mutableStateOf("") }
    var startMonth by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }

    var startHour by remember { mutableStateOf("") }
    var startMinute by remember { mutableStateOf("") }
    var startIsAm by remember { mutableStateOf(true) }

    var endYear by remember { mutableStateOf("") }
    var endMonth by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var endHour by remember { mutableStateOf("") }
    var endMinute by remember { mutableStateOf("") }
    var endIsAm by remember { mutableStateOf(true) }

//    val isButtonEnabled = isDateFormValid(startYear, startMonth, startDate)

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
        color = White,
    ) {
        Column(
            modifier = Modifier,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Column {
                    SelectedTime(
                        title = "시작",
                        isSelected = isStartSelected,
                        onClick = { isStartSelected = true },
                    )
                }
                Column {
                    SelectedTime(
                        title = "종료",
                        isSelected = !isStartSelected,
                        onClick = { isStartSelected = false },
                    )
                }
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .background(color = Gray04, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.9f),
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "날짜",
                        style = Typography.displayLarge.copy(fontSize = 16.sp),
                    )
                    DateInputRow(
                        modifier = Modifier.padding(top = 5.dp),
                        year = if (isStartSelected) startYear else endYear,
                        month = if (isStartSelected) startMonth else endMonth,
                        date = if (isStartSelected) startDate else endDate,
                        onYearChange = { if (isStartSelected) startYear = it else endYear = it },
                        onMonthChange = { if (isStartSelected) startMonth = it else endMonth = it },
                        onDateChange = { if (isStartSelected) startDate = it else endDate = it },
                        focusManager = focusManager,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "시간",
                        style = Typography.displayLarge.copy(fontSize = 16.sp),
                    )
                    TimeInputRow(
                        modifier = Modifier.padding(top = 5.dp),
                        hour = if (isStartSelected) startHour else endHour,
                        minute = if (isStartSelected) startMinute else endMinute,
                        isAm = if (isStartSelected) startIsAm else endIsAm,
                        onHourChange = { if (isStartSelected) startHour = it else endHour = it },
                        onMinuteChange = {
                            if (isStartSelected) startMinute = it else endMinute = it
                        },
                        onAmPmChanged = { if (isStartSelected) startIsAm = it else endIsAm = it },
                        focusManager = focusManager,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            RoundLongButton(
                text = "시간 선택 완료하기",
                onClick = { /*TODO*/ },
//                enabled = isButtonEnabled,
            )
        }
    }
}

@Composable
fun SelectedTime(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
) {
    val textColor = if (isSelected) Brown01 else Gray03
    Column(
        modifier = Modifier.padding(vertical = 10.dp).noRippleClickable { onClick() },
    ) {
        Text(
            text = title,
            style =
                Typography.displayLarge.copy(
                    fontSize = 15.sp,
                    color = textColor,
                ),
        )
        Text(
            text = "2024년 10월 24일",
            style =
                Typography.headlineMedium.copy(
                    fontSize = 22.sp,
                    color = textColor,
                ),
        )
        Text(
            text = "오전 9:00",
            style =
                Typography.displayLarge.copy(
                    fontSize = 15.sp,
                    color = textColor,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeSelectTapPreview() {
    TimeSelectTap()
}
