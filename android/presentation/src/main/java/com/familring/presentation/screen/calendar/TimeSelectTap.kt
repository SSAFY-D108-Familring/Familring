package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeSelectTap(
    modifier: Modifier = Modifier,
    startSchedule: LocalDateTime,
    endSchedule: LocalDateTime,
    isTimeChecked: Boolean,
) {
    var isStartSelected by remember { mutableStateOf(true) }

    var startSchedule by remember { mutableStateOf(startSchedule) }
    var endSchedule by remember { mutableStateOf(endSchedule) }

    val startIsAm by remember { derivedStateOf { startSchedule.hour < 12 } }
    val endIsAm by remember { derivedStateOf { startSchedule.hour < 12 } }

//    var startYear by remember { mutableStateOf(startSchedule.year.toString()) }
//    var startMonth by remember { mutableStateOf(startSchedule.monthValue.toString()) }
//    var startDate by remember { mutableStateOf(startSchedule.dayOfMonth.toString()) }
//
//    var startHour by remember { mutableStateOf(startSchedule.hour.toString()) }
//    var startMinute by remember { mutableStateOf("") }
//    var startIsAm by remember { mutableStateOf(true) }
//
//    var endYear by remember { mutableStateOf("") }
//    var endMonth by remember { mutableStateOf("") }
//    var endDate by remember { mutableStateOf("") }
//
//    var endHour by remember { mutableStateOf("") }
//    var endMinute by remember { mutableStateOf("") }
//    var endIsAm by remember { mutableStateOf(true) }

//    val isButtonEnabled = isDateFormValid(startYear, startMonth, startDate)

    val focusManager = LocalFocusManager.current

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
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
                        schedule = startSchedule,
                        isSelected = isStartSelected,
                        onClick = { isStartSelected = true },
                        isTimeChecked = isTimeChecked,
                    )
                }
                Column {
                    SelectedTime(
                        title = "종료",
                        schedule = endSchedule,
                        isSelected = !isStartSelected,
                        onClick = { isStartSelected = false },
                        isTimeChecked = isTimeChecked,
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
                        year = if (isStartSelected) startSchedule.year.toString() else endSchedule.year.toString(),
                        month = if (isStartSelected) startSchedule.monthValue.toString() else endSchedule.monthValue.toString(),
                        date = if (isStartSelected) startSchedule.dayOfMonth.toString() else endSchedule.dayOfMonth.toString(),
                        onYearChange = {
                            if (isStartSelected) {
                                setScheduleYear(
                                    startSchedule,
                                    it,
                                )
                            } else {
                                setScheduleYear(endSchedule, it)
                            }
                        },
                        onMonthChange = {
                            if (isStartSelected) {
                                setScheduleMonth(
                                    startSchedule,
                                    it,
                                )
                            } else {
                                setScheduleMonth(endSchedule, it)
                            }
                        },
                        onDateChange = {
                            if (isStartSelected) {
                                setScheduleDate(
                                    startSchedule,
                                    it,
                                )
                            } else {
                                setScheduleDate(endSchedule, it)
                            }
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
                                hour = if (isStartSelected) startSchedule.hour.toString() else endSchedule.hour.toString(),
                                minute = if (isStartSelected) startSchedule.minute.toString() else endSchedule.minute.toString(),
                                isAm = if (isStartSelected) startIsAm else endIsAm,
                                onHourChange = {
                                    if (isStartSelected) {
                                        setScheduleHour(startSchedule, it)
                                    } else {
                                        setScheduleHour(endSchedule, it)
                                    }
                                },
                                onMinuteChange = {
                                    if (isStartSelected) {
                                        setScheduleMinute(
                                            startSchedule,
                                            it,
                                        )
                                    } else {
                                        setScheduleMinute(
                                            endSchedule,
                                            it,
                                        )
                                    }
                                },
                                onAmPmChanged = {
                                    if (isStartSelected) {
                                        if (it != startIsAm) setAmPm(startSchedule, it)
                                    } else {
                                        if (it != endIsAm) setAmPm(endSchedule, it)
                                    }
                                },
                                focusManager = focusManager,
                            )
                        }
                    }
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

private fun setScheduleYear(
    schedule: LocalDateTime,
    year: String,
) {
    schedule.withYear(year.toInt())
}

private fun setScheduleMonth(
    schedule: LocalDateTime,
    month: String,
) {
    schedule.withMonth(month.toInt())
}

private fun setScheduleDate(
    schedule: LocalDateTime,
    date: String,
) {
    schedule.withDayOfMonth(date.toInt())
}

private fun setScheduleHour(
    schedule: LocalDateTime,
    hour: String,
) {
    schedule.withHour(hour.toInt())
}

private fun setScheduleMinute(
    schedule: LocalDateTime,
    minute: String,
) {
    schedule.withMinute(minute.toInt())
}

private fun setAmPm(
    schedule: LocalDateTime,
    isAm: Boolean,
) {
    if (isAm) {
        schedule.withHour(schedule.hour - 12)
    } else {
        schedule.withHour(schedule.hour + 12)
    }
}

@Composable
fun SelectedTime(
    modifier: Modifier = Modifier,
    title: String,
    schedule: LocalDateTime,
    isSelected: Boolean,
    isTimeChecked: Boolean,
    onClick: () -> Unit = {},
) {
    val textColor = if (isSelected) Brown01 else Gray03
    Column(
        modifier =
            Modifier
                .padding(vertical = 10.dp)
                .noRippleClickable { onClick() },
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
            text = schedule.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            style =
                Typography.headlineMedium.copy(
                    fontSize = 22.sp,
                    color = textColor,
                ),
        )
        if (isTimeChecked) {
            Text(
                text = schedule.format(DateTimeFormatter.ofPattern("a hh:mm")),
                style =
                    Typography.displayLarge.copy(
                        fontSize = 15.sp,
                        color = textColor,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeSelectTapPreview() {
    TimeSelectTap(
        startSchedule = LocalDateTime.now(),
        endSchedule = LocalDateTime.now(),
        isTimeChecked = false,
    )
}
