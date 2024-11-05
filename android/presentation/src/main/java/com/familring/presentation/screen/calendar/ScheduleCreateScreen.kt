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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.presentation.R
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
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Red02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.theme.Yellow03
import com.familring.presentation.util.noRippleClickable
import java.time.LocalDateTime

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCreateScreen(
    modifier: Modifier = Modifier,
    startSchedule: LocalDateTime = LocalDateTime.now().withHour(9).withMinute(0),
    endSchedule: LocalDateTime = LocalDateTime.now().withHour(12).withMinute(0),
    popUpBackStack: () -> Unit = {},
    profiles: List<Profile> = listOf(),
) {
    var title by remember { mutableStateOf("") }

    var startSchedule by remember { mutableStateOf(startSchedule) }
    var endSchedule by remember { mutableStateOf(endSchedule) }

    var isTimeChecked by remember { mutableStateOf(false) }
    var isNotiChecked by remember { mutableStateOf(false) }

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
    var selectedColorIdx by remember { mutableIntStateOf(0) }

    var isProfileCheckedList by remember {
        mutableStateOf(profiles.map { false }.toMutableStateList())
    }
    var isAllChecked by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val startSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showStartBottomSheet by remember { mutableStateOf(false) }

    val endSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showEndBottomSheet by remember { mutableStateOf(false) }

    val isButtonEnabled = startSchedule.isBefore(endSchedule)

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
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
            ) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier =
                        Modifier
                            .background(
                                color = White,
                            ),
                    singleLine = true,
                    textStyle =
                        Typography.titleLarge.copy(
                            fontSize = 24.sp,
                            color = Black,
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            },
                        ),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "일정 제목",
                                style =
                                    Typography.titleLarge.copy(
                                        fontSize = 24.sp,
                                        color = Gray03,
                                    ),
                            )
                        }
                        innerTextField()
                    },
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SelectedTime(
                        modifier = Modifier.noRippleClickable { showStartBottomSheet = true },
                        title = "시작",
                        schedule = startSchedule,
                        isTimeChecked = isTimeChecked,
                    )
                    SelectedTime(
                        modifier = Modifier.noRippleClickable { showEndBottomSheet = true },
                        title = "종료",
                        schedule = endSchedule,
                        isTimeChecked = isTimeChecked,
                    )
                }
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                    text = "일정 형식을 확인해 주세요!",
                    style =
                        Typography.labelMedium.copy(
                            fontSize = 12.sp,
                            color = if (isButtonEnabled) White else Red01,
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
                        onCheckedChange = {
                            if (!it) {
                                startSchedule.withHour(9).withMinute(0)
                                endSchedule.withHour(12).withMinute(0)
                            }
                            isTimeChecked = it
                        },
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
                        verticalAlignment = Alignment.Bottom,
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
                    colors.forEachIndexed { index, color ->
                        ColorBox(
                            isSelected = (index == selectedColorIdx),
                            color = color,
                            onColorSelected = {
                                selectedColorIdx = index
                            },
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
                LazyVerticalGrid(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    itemsIndexed(profiles) { index, profile ->
                        ZodiacProfileWithNameAndCheckedBox(
                            profile = profile,
                            isChecked = isProfileCheckedList[index],
                            onChecked = { isChecked ->
                                isProfileCheckedList[index] = isChecked

                                isAllChecked =
                                    isProfileCheckedList.all { it }
                            },
                        )
                    }
                    item {
                        AllCheckBox(
                            isChecked = isAllChecked,
                            onChecked = { isChecked ->
                                isAllChecked = isChecked
                                for (i in isProfileCheckedList.indices) {
                                    isProfileCheckedList[i] = isChecked
                                }
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
                enabled = isButtonEnabled,
            )
        }
        if (showStartBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showStartBottomSheet = false
                },
                sheetState = startSheetState,
                containerColor = White,
            ) {
                TimeSelectTap(
                    title = "시작 일정",
                    schedule = startSchedule,
                    isTimeChecked = isTimeChecked,
                    onButtonClicked = {
                        startSchedule = it
                        showStartBottomSheet = false
                    },
                )
            }
        }
        if (showEndBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEndBottomSheet = false
                },
                sheetState = endSheetState,
                containerColor = White,
            ) {
                TimeSelectTap(
                    title = "종료 일정",
                    schedule = endSchedule,
                    isTimeChecked = isTimeChecked,
                    onButtonClicked = {
                        endSchedule = it
                        showEndBottomSheet = false
                    },
                )
            }
        }
    }
}

@Composable
fun ColorBox(
    modifier: Modifier = Modifier,
    color: Color,
    isSelected: Boolean = false,
    onColorSelected: (selectedColor: Color) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .size(30.dp)
                .background(
                    color = color,
                    shape = CircleShape,
                ).noRippleClickable {
                    onColorSelected(color)
                },
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.padding(3.dp),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "ic_check",
                tint = Black,
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
                .noRippleClickable { onChecked(!isChecked) },
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
