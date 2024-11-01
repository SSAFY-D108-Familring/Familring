package com.familring.presentation.screen.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.DailyLife
import com.familring.domain.model.Profile
import com.familring.domain.model.Schedule
import com.familring.presentation.R
import com.familring.presentation.component.IconCustomDropdownMenu
import com.familring.presentation.component.CustomDropdownMenuStyles
import com.familring.presentation.component.CustomTextTab
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.toColor

@Composable
fun CalendarTab(
    modifier: Modifier = Modifier,
    schedules: List<Schedule>,
    dailyLifes: List<DailyLife>,
    navigateToCreateAlbum: () -> Unit = {},
) {
    val tabs = listOf("일정 ${schedules.size}", "일상 ${dailyLifes.size}")
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
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
                        navigateToCreateAlbum = navigateToCreateAlbum,
                    )

                1 ->
                    DailyTab(
                        dailyLifes = dailyLifes,
                    )
            }
        }
    }
}

@Composable
fun DailyTab(
    modifier: Modifier = Modifier,
    dailyLifes: List<DailyLife>,
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
        val pagerState = rememberPagerState(pageCount = { dailyLifes.size })

        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                modifier =
                    Modifier
                        .fillMaxSize(),
                state = pagerState,
                pageSpacing = 14.dp,
                contentPadding = PaddingValues(horizontal = 28.dp),
//                verticalAlignment = Alignment.Top,
            ) { page ->
                DailyItem(
                    dailyLife = dailyLifes[page],
                )
            }
        }
    }
}

@Composable
fun DailyItem(
    modifier: Modifier = Modifier,
    dailyLife: DailyLife,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
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
                modifier = Modifier.size(35.dp),
                profile = dailyLife.profile,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dailyLife.profile.nickName,
                style =
                    Typography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = Gray01,
                    ),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                text = dailyLife.content,
                style =
                    Typography.headlineMedium.copy(
                        fontSize = 17.sp,
                        color = Black,
                    ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.tuna),
                contentDescription = "img_daily",
            )
        }
    }
}

@Composable
fun ScheduleTab(
    modifier: Modifier = Modifier,
    schedules: List<Schedule> = listOf(),
    navigateToCreateAlbum: () -> Unit = {},
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
                    navigateToCreateAlbum = navigateToCreateAlbum,
                )
            }
        }
    }
}

@Composable
fun ScheduleItem(
    modifier: Modifier = Modifier,
    schedule: Schedule,
    navigateToCreateAlbum: () -> Unit = {},
) {
    // drop down menu
    var showDropDownMenu by remember { mutableStateOf(false) }
    var tapOffset by remember { mutableStateOf(DpOffset.Zero) }

    Box(
        modifier =
            modifier
                .clickable { navigateToCreateAlbum() }
                .fillMaxWidth()
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
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = schedule.title,
                        style =
                            Typography.headlineLarge.copy(
                                fontSize = 22.sp,
                            ),
                    )
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
                Text(
                    text = "10월 23일 09:00 - 10월 24일 23:00",
                    style =
                        Typography.labelSmall.copy(
                            fontSize = 12.sp,
                            color = Gray02,
                        ),
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            OverlappingProfileLazyRow(
                modifier =
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                profileSize = 32,
                profiles =
                    listOf(
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                        Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                    ),
            )
            IconCustomDropdownMenu(
                modifier =
                    Modifier
                        .padding(3.dp),
                menuItems =
                    listOf(
                        "수정" to {},
                        "삭제" to {},
                        "앨범 생성" to { navigateToCreateAlbum() },
                    ),
                styles = CustomDropdownMenuStyles(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarTabPreview() {
    CalendarTab(
        dailyLifes = dailyLifes,
        schedules = schedules,
    )
}

val schedules =
    listOf(
        Schedule(
            title = "현진이 생일",
            backgroundColor = "0xFFC9D0FF",
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
            profile =
                Profile(
                    nickName = "엄마미",
                    zodiacImgUrl = "url",
                    backgroundColor = "0xFFFEE222",
                ),
        ),
        DailyLife(
            dailyImgUrl = "",
            content =
                "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요" +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요" +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요" +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 " +
                    "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 ",
            profile =
                Profile(
                    nickName = "엄마미",
                    zodiacImgUrl = "url",
                    backgroundColor = "0xFFFEE222",
                ),
        ),
        DailyLife(
            dailyImgUrl = "",
            content = "현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요 현진이 생일이었어요",
            profile =
                Profile(
                    nickName = "엄마미",
                    zodiacImgUrl = "url",
                    backgroundColor = "0xFFFEE222",
                ),
        ),
    )
