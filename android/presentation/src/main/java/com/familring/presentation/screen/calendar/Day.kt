package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.calendar.DaySchedule
import com.familring.domain.model.calendar.PreviewDaily
import com.familring.domain.model.calendar.PreviewSchedule
import com.familring.presentation.R
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toColor
import com.familring.presentation.util.toLocalDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun Day(
    modifier: Modifier = Modifier,
    daySchedule: DaySchedule?,
    onDayClick: () -> Unit = {},
) {
    if (daySchedule == null) return
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .noRippleClickable { onDayClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val date = daySchedule.date
        val dateStyle =
            if (date == LocalDate.now()) {
                Typography.bodySmall.copy(fontSize = 10.sp, color = White)
            } else {
                Typography.bodySmall.copy(fontSize = 10.sp)
            }
        val dateBackground =
            if (date == LocalDate.now()) {
                Modifier
                    .padding(top = 3.dp, bottom = 5.dp)
                    .drawBehind {
                        drawCircle(
                            color = Green02,
                        )
                    }
            } else {
                Modifier.padding(top = 3.dp, bottom = 5.dp)
            }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier =
                    dateBackground
                        .padding(4.dp),
                text = date.dayOfMonth.toString(),
                style = dateStyle,
                textAlign = TextAlign.Center,
            )
            if (daySchedule.dailies.isNotEmpty()) {
                Icon(
                    modifier =
                        Modifier
                            .padding(top = 3.dp, end = 3.dp)
                            .weight(1f),
                    painter = painterResource(id = R.drawable.img_star),
                    contentDescription = "img_star",
                    tint = Color.Unspecified,
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            val maxVisibleItems = 3
            val visibleItems = MutableList<PreviewSchedule?>(3) { null }
            daySchedule.schedules
                .filter { it.order in 0..2 }
                .sortedBy { it.order }
                .forEach { schedule -> visibleItems[schedule.order] = schedule }

            items(visibleItems) { schedule ->
                if (schedule != null) {
                    val isPrevConnected =
                        schedule.startTime
                            .toLocalDate()
                            .isBefore(daySchedule.date) and (daySchedule.date.dayOfWeek != DayOfWeek.SUNDAY)
                    val isNextConnected =
                        schedule.endTime
                            .toLocalDate()
                            .isAfter(daySchedule.date) and (daySchedule.date.dayOfWeek != DayOfWeek.SATURDAY)
                    val showText =
                        daySchedule.date.dayOfWeek == DayOfWeek.SUNDAY || daySchedule.date == schedule.startTime.toLocalDate()

                    Schedule(
                        modifier = Modifier.fillMaxWidth(),
                        isPrevConnected = isPrevConnected,
                        isNextConnected = isNextConnected,
                        showText = showText,
                        schedule = schedule,
                    )
                } else {
                    Schedule(
                        modifier = Modifier.fillMaxWidth(),
                        schedule = schedule,
                    )
                }
            }

            if (daySchedule.schedules.size > maxVisibleItems) {
                item {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 2.dp),
                        textAlign = TextAlign.End,
                        text = "...",
                        style =
                            Typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = Green03,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun Schedule(
    modifier: Modifier = Modifier,
    isPrevConnected: Boolean = false,
    isNextConnected: Boolean = false,
    showText: Boolean = false,
    schedule: PreviewSchedule?,
) {
    if (schedule == null) {
        Text(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 5.dp,
                        vertical = 3.dp,
                    ),
            text = "",
            style = Typography.bodySmall.copy(fontSize = 8.sp),
            maxLines = 1,
        )
    } else {
        val background =
            if (isPrevConnected && isNextConnected) {
                Modifier
                    .background(
                        color = schedule.color.toColor(),
                        shape = RoundedCornerShape(0.dp),
                    )
            } else if (isPrevConnected) {
                Modifier
                    .padding(end = 2.dp)
                    .background(
                        color = schedule.color.toColor(),
                        RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp),
                    )
            } else if (isNextConnected) {
                Modifier
                    .padding(start = 1.dp)
                    .background(
                        color = schedule.color.toColor(),
                        RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp),
                    )
            } else {
                Modifier
                    .padding(horizontal = 2.dp)
                    .background(
                        color = schedule.color.toColor(),
                        RoundedCornerShape(3.dp),
                    )
            }

        Text(
            modifier =
                modifier
                    .fillMaxWidth()
                    .then(background)
                    .padding(
                        horizontal = 5.dp,
                        vertical = 3.dp,
                    ),
            text = if (showText) schedule.title else "",
            style = Typography.bodySmall.copy(fontSize = 8.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SchedulePreview() {
    Schedule(
        modifier = Modifier.width(50.dp),
        schedule =
            PreviewSchedule(
                id = 0,
                title = "Meeting with Team",
                startTime = LocalDateTime.parse("2024-10-01T10:00:00"),
                endTime = LocalDateTime.parse("2024-10-03T10:00:00"),
                color = "0xFFFEE6C9",
            ),
        showText = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun DayPreview() {
    Day(
        modifier = Modifier.size(50.dp, 90.dp),
        daySchedule =
            DaySchedule(
                date = "2024-11-07".toLocalDate(),
                schedules =
                    listOf(
                        PreviewSchedule(
                            id = 0,
                            title = "Meeting with Team",
                            startTime = LocalDateTime.parse("2024-10-01T10:00:00"),
                            endTime = LocalDateTime.parse("2024-10-03T12:00:00"),
                            color = "0xFF000000",
                            order = 0,
                        ),
                        PreviewSchedule(
                            id = 0,
                            title = "Meeting with Team",
                            startTime = LocalDateTime.parse("2024-10-01T10:00:00"),
                            endTime = LocalDateTime.parse("2024-10-03T12:00:00"),
                            color = "0xFF000000",
                            order = 2,
                        ),
                    ),
                dailies =
                    listOf(
                        PreviewDaily(
                            id = 0,
                            createdAt = LocalDateTime.now(),
                        ),
                    ),
            ),
    )
}
