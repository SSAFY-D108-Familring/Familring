package com.familring.presentation.screen.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.DaySchedule
import com.familring.domain.Schedule
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.toLocalDate
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthGrid(
    modifier: Modifier = Modifier,
    date: LocalDate,
    daySchedules: List<DaySchedule> = listOf(),
    onDayClick: (LocalDate) -> Unit = {},
) {
    val yearMonth = YearMonth.of(date.year, date.month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1)
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일이 0이 되도록 조정

    // 42칸을 생성하고 필요한 위치에 날짜를 배치
    val days =
        (1..42).map { day ->
            when {
                day <= startDayOfWeek -> null // 이전 달 빈 칸
                day - startDayOfWeek <= daysInMonth -> daySchedules[day - startDayOfWeek - 1] // 현재 달의 날짜
                else -> null // 다음 달 빈 칸
            }
        }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                val textColor = if (day == "일") Red01 else Black
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style =
                        Typography.displayMedium.copy(
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.5f),
                        ),
                )
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val cellHeight = maxHeight / 6
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(7),
            ) {
                items(days) { day ->
                    Day(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(cellHeight),
                        daySchedule = day,
                        onDayClick = {
                            day?.date?.let {
                                onDayClick(it.toLocalDate())
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthGridPreview() {
    MonthGrid(
        modifier = Modifier.fillMaxSize(),
        date = LocalDate.of(2024, 10, 1),
        daySchedules = daySchedules,
    )
}

val sampleSchedules =
//    listOf<Schedule>()
    listOf(
        Schedule("Meeting with Team", "0xFFFEE6C9"),
        Schedule("Doctor's Appointment", "0xFFD2F0FF"),
    )
val daySchedules =
    (1..31).map { day ->
        DaySchedule(
            date = "2024-10-${day.toString().padStart(2, '0')}",
            schedules = if (day % 3 == 0) sampleSchedules else emptyList(),
        )
    }
