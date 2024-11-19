package com.familring.presentation.screen.calendar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.familring.domain.model.calendar.DaySchedule
import com.familring.domain.model.calendar.PreviewSchedule
import com.familring.presentation.util.toLocalDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun MonthGrid(
    modifier: Modifier = Modifier,
    date: LocalDate,
    daySchedules: List<DaySchedule> = listOf(),
    onDayClick: (DaySchedule) -> Unit = { },
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

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
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
                        if (day != null) {
                            onDayClick(day)
                        }
                    },
                )
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

val samplePreviewSchedules =
    listOf(
        PreviewSchedule(
            id = 0,
            title = "Meeting with Team",
            startTime = LocalDateTime.parse("2024-10-01T10:00:00"),
            endTime = LocalDateTime.parse("2024-10-03T12:00:00"),
            color = "0xFFFAA6C9",
        ),
        PreviewSchedule(
            id = 1,
            title = "Meeting with Team",
            startTime = LocalDateTime.parse("2024-10-06T10:00:00"),
            endTime = LocalDateTime.parse("2024-10-06T12:00:00"),
            color = "0xFFFEE6C9",
        ),
    )
val daySchedules =
    (1..31).map { day ->
        DaySchedule(
            date = "2024-10-${day.toString().padStart(2, '0')}".toLocalDate(),
            schedules = if (day % 3 == 0) samplePreviewSchedules else emptyList(),
            dailies = listOf(),
        )
    }
