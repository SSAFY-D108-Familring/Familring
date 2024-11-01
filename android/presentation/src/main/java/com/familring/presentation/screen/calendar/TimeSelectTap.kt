package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.isDateFormValid
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeSelectTap(
    modifier: Modifier = Modifier,
    title: String,
    schedule: LocalDateTime,
    isTimeChecked: Boolean,
    onButtonClicked: (selectedSchedule: LocalDateTime) -> Unit = {},
) {
    var year by remember { mutableStateOf(schedule.format(DateTimeFormatter.ofPattern("yyyy"))) }
    var month by remember { mutableStateOf(schedule.format(DateTimeFormatter.ofPattern("MM"))) }
    var date by remember { mutableStateOf(schedule.format(DateTimeFormatter.ofPattern("dd"))) }

    var hour by remember { mutableStateOf(schedule.format(DateTimeFormatter.ofPattern("hh"))) }
    var minute by remember { mutableStateOf(schedule.format(DateTimeFormatter.ofPattern("mm"))) }
    var isAm by remember { mutableStateOf(schedule.hour < 12) }

    val isButtonEnabled =
        isDateFormValid(
            year,
            month,
            date,
            hour,
            minute,
        )

    val focusManager = LocalFocusManager.current

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        color = White,
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 10.dp, start = 5.dp),
                text = title,
                style =
                    Typography.headlineMedium.copy(
                        fontSize = 22.sp,
                    ),
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
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
                        year = year,
                        month = month,
                        date = date,
                        onYearChange = {
                            year = it
                        },
                        onMonthChange = {
                            month = it
                        },
                        onDateChange = {
                            date = it
                        },
                        focusManager = focusManager,
                    )
                    if (isTimeChecked) {
                        Column {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "시간",
                                style = Typography.displayLarge.copy(fontSize = 16.sp),
                            )
                            TimeInputRow(
                                modifier = Modifier.padding(top = 5.dp),
                                hour = hour,
                                minute = minute,
                                isAm = isAm,
                                onHourChange = {
                                    hour = it
                                },
                                onMinuteChange = {
                                    minute = it
                                },
                                onAmPmChanged = {
                                    isAm = it
                                },
                                focusManager = focusManager,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 5.dp, bottom = 10.dp, start = 5.dp),
                text = "일정 형식을 확인해 주세요!",
                style =
                    Typography.labelMedium.copy(
                        fontSize = 12.sp,
                        color = if (isButtonEnabled) White else Red01,
                    ),
            )
            RoundLongButton(
                text = "시간 선택 완료하기",
                onClick = {
                    onButtonClicked(
                        LocalDateTime.of(
                            year.toInt(),
                            month.toInt(),
                            date.toInt(),
                            calculateAmPmHour(hour.toInt(), isAm),
                            minute.toInt(),
                        ),
                    )
                },
                enabled = isButtonEnabled,
            )
        }
    }
}

private fun calculateAmPmHour(
    hour: Int,
    isAm: Boolean,
): Int =
    if (isAm) {
        if (hour < 12) hour else hour - 12
    } else {
        if (hour < 12) hour + 12 else hour
    }

@Composable
fun SelectedTime(
    modifier: Modifier = Modifier,
    title: String,
    schedule: LocalDateTime,
    isTimeChecked: Boolean,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style =
                Typography.displayLarge.copy(
                    fontSize = 15.sp,
                    color = Brown01,
                ),
        )
        Text(
            text = schedule.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            style =
                Typography.headlineMedium.copy(
                    fontSize = 22.sp,
                    color = Brown01,
                ),
        )
        if (isTimeChecked) {
            Text(
                text = schedule.format(DateTimeFormatter.ofPattern("a hh:mm")),
                style =
                    Typography.displayLarge.copy(
                        fontSize = 15.sp,
                        color = Brown01,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeSelectTapPreview() {
    TimeSelectTap(
        title = "시작 일정",
        schedule = LocalDateTime.now(),
        isTimeChecked = true,
    )
}
