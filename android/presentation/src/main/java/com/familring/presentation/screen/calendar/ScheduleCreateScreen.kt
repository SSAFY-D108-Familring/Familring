package com.familring.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.Profile
import com.familring.presentation.component.CustomCheckBox
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Blue01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green08
import com.familring.presentation.theme.Orange01
import com.familring.presentation.theme.Pink02
import com.familring.presentation.theme.Red02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.theme.Yellow03

@Composable
fun ScheduleCreateRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
) {
    ScheduleCreateScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        profiles = profiles,
    )
}

@Composable
fun ScheduleCreateScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    profiles: List<Profile> = listOf(),
) {
    var title by remember { mutableStateOf("") }

    var isTimeChecked by remember { mutableStateOf(false) }
    var isNotiChecked by remember { mutableStateOf(false) }

    var isProfileCheckedList by remember {
        mutableStateOf(profiles.map { false }.toMutableStateList())
    }
    var isAllChecked by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "일정 추가",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                textStyle =
                    Typography.titleLarge.copy(
                        fontSize = 24.sp,
                        color = Black,
                    ),
                placeholder = {
                    Text(
                        text = "일정 제목",
                        style =
                            Typography.titleLarge.copy(
                                fontSize = 24.sp,
                                color = Gray03,
                            ),
                    )
                },
                singleLine = true,
                maxLines = 1,
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Black,
                        focusedTextColor = Black,
                        unfocusedTextColor = Gray03,
                    ),
            )
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
            ) {
                Text(
                    text = "10월 24일 목 오전 9:00 - 오후 12:00",
                    style =
                        Typography.headlineLarge.copy(
                            fontSize = 18.sp,
                            color = Black,
                        ),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "시간 설정",
                        style =
                            Typography.headlineSmall.copy(
                                fontSize = 18.sp,
                                color = Black,
                            ),
                    )
                    Switch(
                        checked = isTimeChecked,
                        onCheckedChange = { isTimeChecked = it },
                        colors =
                            SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = Green02,
                                uncheckedThumbColor = White,
                                uncheckedTrackColor = Gray03,
                                uncheckedBorderColor = Color.Transparent,
                            ),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "알림",
                            style =
                                Typography.headlineSmall.copy(
                                    fontSize = 18.sp,
                                    color = Black,
                                ),
                        )
                        Text(
                            modifier =
                                Modifier
                                    .padding(start = 5.dp),
                            text = "1일 전 오전 9시",
                            style =
                                Typography.displayLarge.copy(
                                    fontSize = 12.sp,
                                    color = Gray02,
                                ),
                        )
                    }
                    Switch(
                        checked = isNotiChecked,
                        onCheckedChange = { isNotiChecked = it },
                        colors =
                            SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = Green02,
                                checkedBorderColor = Color.Transparent,
                                uncheckedThumbColor = White,
                                uncheckedTrackColor = Gray03,
                                uncheckedBorderColor = Color.Transparent,
                            ),
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Text(
                    text = "표시 색상",
                    style =
                        Typography.headlineSmall.copy(
                            fontSize = 18.sp,
                            color = Black,
                        ),
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    val colors =
                        listOf(
                            Red02,
                            Orange01,
                            Yellow03,
                            Green08,
                            Blue01,
                            Pink02,
                            Gray02,
                            Black,
                        )
                    colors.forEach { color ->
                        Box(
                            modifier =
                                Modifier
                                    .size(30.dp)
                                    .background(
                                        color = color,
                                        shape = CircleShape,
                                    ),
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Text(
                    text = "함께하는 구성원",
                    style =
                        Typography.headlineSmall.copy(
                            fontSize = 18.sp,
                            color = Black,
                        ),
                )
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    itemsIndexed(profiles) { index, profile ->
                        ZodiacProfileWithNameAndCheckedBox(
                            profile = profile,
                            isChecked = isProfileCheckedList[index],
                            onChecked = {
                                isProfileCheckedList[index] = it
                                if (isAllChecked) {
                                    isAllChecked = false
                                }
                            },
                        )
                    }
                    item {
                        AllCheckBox(
                            isChecked = isAllChecked,
                            onChecked = { checked ->
                                isAllChecked = checked
                                isProfileCheckedList =
                                    isProfileCheckedList
                                        .map { checked }
                                        .toMutableStateList()
                            },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            RoundLongButton(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "일정 추가하기",
                onClick = {},
            )
        }
    }
}

@Composable
fun AllCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onChecked: (checked: Boolean) -> Unit = {},
) {
    val textColor = if (isChecked) Black else Gray02

    Row(
        modifier =
            modifier
                .padding(vertical = 10.dp)
                .clickable { onChecked(!isChecked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomCheckBox(
            isChecked = isChecked,
            onChecked = onChecked,
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = "가족 모두",
            style =
                Typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    color = textColor,
                ),
        )
    }
}

@Composable
fun ZodiacProfileWithNameAndCheckedBox(
    modifier: Modifier = Modifier,
    profile: Profile,
    isChecked: Boolean = false,
    onChecked: (checked: Boolean) -> Unit = {},
) {
    val textColor = if (isChecked) Black else Gray02

    Row(
        modifier =
            modifier
                .padding(vertical = 10.dp)
                .clickable { onChecked(!isChecked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomCheckBox(
            isChecked = isChecked,
            onChecked = onChecked,
        )
        Spacer(modifier = Modifier.width(10.dp))
        ZodiacBackgroundProfile(
            modifier = Modifier.size(30.dp),
            profile = profile,
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = profile.nickName,
            style =
                Typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    color = textColor,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ZodiacProfileWithNamePreview() {
//    AllCheckBox()
    ZodiacProfileWithNameAndCheckedBox(
        profile =
            Profile(
                zodiacImgUrl = "",
                nickName = "홍길동",
                backgroundColor = "0xFFC9D0FF",
            ),
    )
}

@Preview
@Composable
private fun ScheduleCreateScreenPreview() {
    ScheduleCreateScreen(
        profiles = profiles,
    )
}

val profiles =
    listOf(
        Profile(
            zodiacImgUrl = "",
            nickName = "홍길동",
            backgroundColor = "0xFFC9D0FF",
        ),
        Profile(
            zodiacImgUrl = "",
            nickName = "홍길동",
            backgroundColor = "0xFFC9D0FF",
        ),
        Profile(
            zodiacImgUrl = "",
            nickName = "홍길동",
            backgroundColor = "0xFFC9D0FF",
        ),
        Profile(
            zodiacImgUrl = "",
            nickName = "홍길동",
            backgroundColor = "0xFFC9D0FF",
        ),
    )
