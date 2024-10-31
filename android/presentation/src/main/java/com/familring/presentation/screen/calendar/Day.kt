package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.DaySchedule
import com.familring.domain.model.Schedule
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toColor
import com.familring.presentation.util.toLocalDate
import java.time.LocalDate

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
        val date = daySchedule.date.toLocalDate()
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

        Text(
            modifier =
                dateBackground
                    .padding(4.dp),
            text = date.dayOfMonth.toString(),
            style = dateStyle,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(1.dp),
        ) {
            val maxVisibleItems = 3

            items(daySchedule.schedules.take(maxVisibleItems)) { schedule ->
                Schedule(
                    modifier = Modifier.width(50.dp),
                    schedule = schedule,
                )
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
    schedule: Schedule,
) {
    Text(
        modifier =
            modifier
                .background(
                    color = schedule.backgroundColor.toColor(),
                    shape = RoundedCornerShape(2.dp),
                ).padding(
                    horizontal = 5.dp,
                    vertical = 3.dp,
                ),
        text = schedule.title,
        style = Typography.bodySmall.copy(fontSize = 8.sp),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Preview(showBackground = true)
@Composable
private fun SchedulePreview() {
    Schedule(
        modifier = Modifier.width(50.dp),
        schedule =
            Schedule(
                title = "Meeting w/ Chris Meeting w/ Chris",
                backgroundColor = "0xFFFFEAB0",
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun DayPreview() {
    Day(
        modifier = Modifier.size(50.dp, 90.dp),
        daySchedule =
            DaySchedule(
                date = "2024-10-31",
                schedules =
                    listOf(
                        Schedule(
                            title = "Meeting w/ Chris Meeting w/ Chris",
                            backgroundColor = "0xFFFFEAB0",
                        ),
                        Schedule(
                            title = "Meeting w/ Chris Meeting w/ Chris",
                            backgroundColor = "0xFFFFEAB0",
                        ),
                        Schedule(
                            title = "Meeting w/ Chris Meeting w/ Chris",
                            backgroundColor = "0xFFFFEAB0",
                        ),
                        Schedule(
                            title = "Meeting w/ Chris Meeting w/ Chris",
                            backgroundColor = "0xFFFFEAB0",
                        ),
                    ),
            ),
    )
}
