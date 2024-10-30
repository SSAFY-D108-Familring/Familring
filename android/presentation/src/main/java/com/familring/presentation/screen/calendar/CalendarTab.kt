package com.familring.presentation.screen.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.Profile
import com.familring.domain.Schedule
import com.familring.presentation.R
import com.familring.presentation.component.CustomTextTab
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.toColor

@Composable
fun CalendarTab(
    modifier: Modifier = Modifier,
    schedules: List<Schedule>,
    dailySize: Int,
    navigateToAlbum: () -> Unit = {},
) {
    val tabs = listOf("일정 ${schedules.size}", "일상 $dailySize")
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
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            when (selectedItemIndex) {
                0 ->
                    ScheduleTab(
                        schedules = schedules,
                    )

                1 -> {}
            }
        }
    }
}

@Composable
fun ScheduleTab(
    modifier: Modifier = Modifier,
    schedules: List<Schedule>,
    navigateToAlbum: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(schedules) { schedule ->
            ScheduleItem(
                schedule = schedule,
            )
        }
    }
}

@Composable
fun ScheduleItem(
    modifier: Modifier = Modifier,
    schedule: Schedule,
    navigateToAlbum: () -> Unit = {},
) {
    // drop down menu
    var showDropDownMenu by remember { mutableStateOf(false) }
    var tapOffset by remember { mutableStateOf(DpOffset.Zero) }

    Box(
        modifier =
            modifier
                .clickable { navigateToAlbum() }
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
            Icon(
                modifier =
                    Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    tapOffset = DpOffset(it.x.toDp(), it.y.toDp())
                                    Log.d("tapOffset", tapOffset.toString())
                                    showDropDownMenu = true
                                },
                            )
                        }.padding(3.dp),
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "ic_more",
            )
        }
    }

    DropdownMenu(
        modifier = Modifier.wrapContentSize(),
        expanded = showDropDownMenu,
        onDismissRequest = { showDropDownMenu = false },
        containerColor = White,
        offset = tapOffset,
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "일정 수정")
            },
            onClick = { /*TODO*/ },
        )
        DropdownMenuItem(
            text = {
                Text(text = "일정 삭제")
            },
            onClick = { /*TODO*/ },
        )
        DropdownMenuItem(
            text = {
                Text(text = "앨범 생성")
            },
            onClick = { /*TODO*/ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarTabPreview() {
    CalendarTab(
        dailySize = 5,
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
