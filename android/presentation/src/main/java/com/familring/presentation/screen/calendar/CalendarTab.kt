package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.familring.domain.mapper.toProfile
import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.model.calendar.Schedule
import com.familring.presentation.R
import com.familring.presentation.component.CustomTextTab
import com.familring.presentation.component.IconCustomDropBoxStyles
import com.familring.presentation.component.IconCustomDropdownMenu
import com.familring.presentation.component.ImageLoadingProgress
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toColor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CalendarTab(
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
    schedules: List<Schedule>,
    dailyLifes: List<DailyLife>,
    showDeleteScheduleDialog: (Long) -> Unit,
    showDeleteDailyDialog: (Long) -> Unit,
    createAlbum: (Long, String) -> Unit = { _, _ -> },
    navigateToModifySchedule: (Schedule) -> Unit = {},
    navigateToModifyDaily: (DailyLife) -> Unit = {},
    navigateToAlbum: (Long, Boolean) -> Unit,
) {
    val tabs = listOf("일정 ${schedules.size}", "일상 ${dailyLifes.size}")
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier =
            modifier
                .fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier,
                text = date.toString(),
                style =
                    Typography.headlineSmall.copy(
                        fontSize = 18.sp,
                        color = Gray01,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            CustomTextTab(
                selectedItemIndex = selectedItemIndex,
                tabs = tabs,
                onClick = { selectedItemIndex = it },
                selectedTextColor = Green02,
            )
            when (selectedItemIndex) {
                0 ->
                    ScheduleTab(
                        schedules = schedules,
                        createAlbum = createAlbum,
                        showDeleteDialog = showDeleteScheduleDialog,
                        navigateToModifySchedule = navigateToModifySchedule,
                        navigateToAlbum = navigateToAlbum,
                    )

                1 ->
                    DailyTab(
                        dailyLifes = dailyLifes,
                        navigateToModifyDaily = navigateToModifyDaily,
                        showDeleteDailyDialog = showDeleteDailyDialog,
                    )
            }
        }
    }
}

@Composable
fun DailyTab(
    modifier: Modifier = Modifier,
    dailyLifes: List<DailyLife>,
    navigateToModifyDaily: (DailyLife) -> Unit = {},
    showDeleteDailyDialog: (Long) -> Unit,
) {
    if (dailyLifes.isEmpty()) {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "아직 등록된 일상이 없어요!",
                style =
                    Typography.labelMedium.copy(
                        fontSize = 18.sp,
                        color = Gray02,
                    ),
            )
        }
    } else {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp),
        ) {
            LazyColumn(
                modifier =
                    modifier
                        .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(dailyLifes) { dailyLife ->
                    DailyItem(
                        dailyLife = dailyLife,
                        navigateToModifyDaily = navigateToModifyDaily,
                        showDeleteDailyDialog = showDeleteDailyDialog,
                    )
                }
            }
        }
    }
}

@Composable
fun DailyItem(
    modifier: Modifier = Modifier,
    dailyLife: DailyLife,
    navigateToModifyDaily: (DailyLife) -> Unit,
    showDeleteDailyDialog: (Long) -> Unit,
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Gray03, shape = RoundedCornerShape(12.dp))
                .background(color = White)
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ZodiacBackgroundProfile(
                profile = dailyLife.profile,
                size = 35,
                paddingValue = 5,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = dailyLife.profile.nickname,
                style =
                    Typography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = Gray01,
                    ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (dailyLife.myPost) {
                Row {
                    Icon(
                        modifier =
                            Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    navigateToModifyDaily(dailyLife)
                                },
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "ic_edit",
                        tint = Gray01,
                    )
                    Icon(
                        modifier =
                            Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    showDeleteDailyDialog(dailyLife.dailyId)
                                },
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "ic_delete",
                        tint = Gray01,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            if (dailyLife.content.isNotBlank() and dailyLife.content.isNotEmpty()) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                    textAlign = TextAlign.Start,
                    text = dailyLife.content,
                    style =
                        Typography.headlineMedium.copy(
                            fontSize = 17.sp,
                            color = Black,
                        ),
                )
            }
            Column {
                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                    model = dailyLife.dailyImgUrl,
                    contentDescription = "img_daily",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                    onLoading = {
                        isImageLoaded = false
                    },
                    onSuccess = {
                        isImageLoaded = true
                    },
                )
                if (!isImageLoaded) {
                    ImageLoadingProgress()
                }
            }
        }
    }
}

@Composable
fun ScheduleTab(
    modifier: Modifier = Modifier,
    schedules: List<Schedule> = listOf(),
    showDeleteDialog: (Long) -> Unit = {},
    createAlbum: (Long, String) -> Unit = { _, _ -> },
    navigateToModifySchedule: (Schedule) -> Unit = {},
    navigateToAlbum: (Long, Boolean) -> Unit = { _, _ -> },
) {
    if (schedules.isEmpty()) {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "아직 등록된 일정이 없어요!",
                style =
                    Typography.labelMedium.copy(
                        fontSize = 18.sp,
                        color = Gray02,
                    ),
            )
        }
    } else {
        LazyColumn(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 20.dp),
        ) {
            items(schedules) { schedule ->
                ScheduleItem(
                    schedule = schedule,
                    showDeleteDialog = showDeleteDialog,
                    navigateToModifySchedule = navigateToModifySchedule,
                    createAlbum = createAlbum,
                    navigateToAlbum = navigateToAlbum,
                )
            }
        }
    }
}

@Composable
fun ScheduleItem(
    modifier: Modifier = Modifier,
    schedule: Schedule,
    createAlbum: (Long, String) -> Unit = { _, _ -> },
    showDeleteDialog: (Long) -> Unit = {},
    navigateToModifySchedule: (Schedule) -> Unit = {},
    navigateToAlbum: (Long, Boolean) -> Unit = { _, _ -> },
) {
    Box(
        modifier =
            modifier
                .clickable {
                    if (schedule.albumId != null) {
                        navigateToAlbum(
                            schedule.albumId!!,
                            true,
                        )
                    }
                }.fillMaxWidth()
                .padding(top = 15.dp, start = 10.dp, bottom = 15.dp),
    ) {
        Row {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier =
                    Modifier
                        .padding(top = 5.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = schedule.backgroundColor.toColor()),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = schedule.title,
                        style =
                            Typography.headlineLarge.copy(
                                fontSize = 22.sp,
                            ),
                    )
                    if (schedule.albumId != null) {
                        Icon(
                            modifier =
                                Modifier
                                    .padding(4.dp)
                                    .size(16.dp),
                            painter = painterResource(id = R.drawable.ic_gallery),
                            contentDescription = "ic_album",
                            tint = Gray02,
                        )
                    }
                }
                Text(
                    text =
                        getScheduleText(
                            hasTime = schedule.hasTime,
                            startSchedule = schedule.startTime,
                            endSchedule = schedule.endTime,
                        ),
                    style =
                        Typography.labelSmall.copy(
                            fontSize = 12.sp,
                            color = Gray02,
                        ),
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            OverlappingProfileLazyRow(
                modifier = Modifier.align(Alignment.CenterVertically),
                profileSize = 32,
                profiles =
                    schedule.familyMembers
                        .filter { it.attendanceStatus }
                        .map { it.toProfile() },
            )
            IconCustomDropdownMenu(
                modifier =
                    Modifier
                        .padding(3.dp),
                menuItems =
                    if (schedule.albumId == null) {
                        listOf(
                            "수정" to { navigateToModifySchedule(schedule) },
                            "삭제" to { showDeleteDialog(schedule.scheduleId) },
                            "앨범 생성" to { createAlbum(schedule.scheduleId, schedule.title) },
                        )
                    } else {
                        listOf(
                            "수정" to { navigateToModifySchedule(schedule) },
                            "삭제" to { showDeleteDialog(schedule.scheduleId) },
                        )
                    },
                styles = IconCustomDropBoxStyles(),
            )
        }
    }
}

private fun getScheduleText(
    hasTime: Boolean,
    startSchedule: LocalDateTime,
    endSchedule: LocalDateTime,
): String {
    val formatWithTime = DateTimeFormatter.ofPattern("MM월 dd일 a hh:mm")
    val formatWithoutTime = DateTimeFormatter.ofPattern("MM월 dd일")
    val formatWithOnlyTime = DateTimeFormatter.ofPattern("a hh:mm")

    val text =
        if (hasTime) {
            if (startSchedule.toLocalDate() == endSchedule.toLocalDate()) {
                startSchedule.format(formatWithTime) +
                    " - " + endSchedule.format(formatWithOnlyTime)
            } else {
                startSchedule.format(formatWithTime) +
                    " - " + endSchedule.format(formatWithTime)
            }
        } else {
            if (startSchedule.toLocalDate() == endSchedule.toLocalDate()) {
                startSchedule.format(formatWithoutTime)
            } else {
                startSchedule.format(formatWithoutTime) +
                    " - " + endSchedule.format(formatWithoutTime)
            }
        }

    return text
}

@Preview(showBackground = true)
@Composable
private fun CalendarTabPreview() {
    CalendarTab(
        dailyLifes = dailyLifes,
        schedules = schedules,
        createAlbum = { _, _ -> },
        navigateToModifySchedule = {},
        navigateToAlbum = { _, _ -> },
        showDeleteScheduleDialog = {},
        showDeleteDailyDialog = {},
    )
}

val schedules =
    listOf(
        Schedule(
            title = "현진이 생일",
            backgroundColor = "0xFFC9D0FF",
            albumId = 1L,
        ),
        Schedule(
            title = "현진이 생일",
            backgroundColor = "0xFFC9D0FF",
        ),
        Schedule(
            title = "현진이 생일",
            backgroundColor = "0xFFC9D0FF",
        ),
    )

val dailyLifes =
    listOf(
        DailyLife(
            dailyImgUrl = "",
            content = "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요",
            userZodiacSign = "",
            userNickname = "",
            userColor = "0xFFFEE222",
        ),
        DailyLife(
            dailyImgUrl = "",
            content = "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요",
            userZodiacSign = "",
            userNickname = "",
            userColor = "0xFFFEE222",
        ),
        DailyLife(
            dailyImgUrl = "",
            content = "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요",
            userZodiacSign = "",
            userNickname = "",
            userColor = "0xFFFEE222",
        ),
    )
