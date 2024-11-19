package com.familring.presentation.screen.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.Profile
import com.familring.domain.model.calendar.Schedule
import com.familring.domain.request.ScheduleCreateRequest
import com.familring.presentation.R
import com.familring.presentation.component.CustomCheckBox
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.component.dialog.TwoButtonTextDialog
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
import com.familring.presentation.util.toColor
import com.familring.presentation.util.toColorLongString
import java.time.LocalDateTime

@Composable
fun ScheduleCreateRoute(
    modifier: Modifier = Modifier,
    targetSchedule: Schedule,
    isModify: Boolean,
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val uiState by scheduleViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scheduleViewModel.event.collect { event ->
            when (event) {
                is ScheduleUiEvent.Success -> {
                    popUpBackStack()
                }

                is ScheduleUiEvent.Error -> {
                    showSnackBar(event.message)
                }
            }
        }
    }

    if (!uiState.isLoading) {
        ScheduleCreateScreen(
            modifier = modifier,
            targetSchedule = targetSchedule,
            isModify = isModify,
            state = uiState,
            createSchedule = scheduleViewModel::createSchedule,
            updateSchedule = scheduleViewModel::updateSchedule,
            popUpBackStack = popUpBackStack,
            showSnackBar = showSnackBar,
        )
    } else {
        LoadingDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCreateScreen(
    modifier: Modifier = Modifier,
    targetSchedule: Schedule,
    isModify: Boolean = false,
    state: ScheduleUiState,
    createSchedule: (ScheduleCreateRequest) -> Unit = {},
    updateSchedule: (Long, ScheduleCreateRequest) -> Unit = { _, _ -> },
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
) {
    var title by remember { mutableStateOf("") }

    var startSchedule by remember { mutableStateOf(LocalDateTime.now().withHour(9).withMinute(0)) }
    var endSchedule by remember { mutableStateOf(LocalDateTime.now().withHour(12).withMinute(0)) }

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
        )
    var selectedColorIdx by remember { mutableIntStateOf(0) }

    val isProfileCheckedList by remember {
        derivedStateOf { state.familyProfiles.map { false }.toMutableStateList() }
    }

    var isAllChecked by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val startSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showStartBottomSheet by remember { mutableStateOf(false) }

    val endSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showEndBottomSheet by remember { mutableStateOf(false) }

    val isButtonEnabled by remember {
        derivedStateOf {
            startSchedule.isBefore(endSchedule) &&
                title.isNotBlank() &&
                isProfileCheckedList.any { it } &&
                selectedColorIdx in colors.indices
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(targetSchedule) {
        if (isModify) {
            title = targetSchedule.title
            startSchedule = targetSchedule.startTime
            endSchedule = targetSchedule.endTime
            isTimeChecked = targetSchedule.hasTime
            isNotiChecked = targetSchedule.hasNotification
            selectedColorIdx = colors.indexOf(targetSchedule.backgroundColor.toColor())

            isProfileCheckedList.clear()
            isProfileCheckedList.addAll(targetSchedule.familyMembers.map { it.attendanceStatus })

            isAllChecked = isProfileCheckedList.all { it }
        }
    }

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
                        text = if (!isModify) "일정 추가" else "일정 수정",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = { showDialog = true },
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
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
                            color = if (startSchedule.isBefore(endSchedule)) White else Red01,
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
                    Column(
//                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            text = "등록 알림",
                            style =
                                Typography.headlineSmall.copy(
                                    fontSize = 18.sp,
                                    color = Black,
                                ),
                        )
                        Text(
                            modifier =
                                Modifier
                                    .padding(top = 3.dp),
                            text = "함께하는 구성원에게 등록 알림을 보내요",
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
                    itemsIndexed(state.familyProfiles) { index, profile ->
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
                text = if (!isModify) "일정 추가하기" else "일정 수정하기",
                onClick = {
                    val newSchedule =
                        ScheduleCreateRequest(
                            title = title,
                            color = colors[selectedColorIdx].toColorLongString(),
                            hasTime = isTimeChecked,
                            hasNotification = isNotiChecked,
                            startTime = startSchedule,
                            endTime = endSchedule,
                            attendances =
                                isProfileCheckedList.mapIndexed { index, isChecked ->
                                    ScheduleCreateRequest.Attendance(
                                        userId = state.familyMembers[index].userId,
                                        attendanceStatus = isChecked,
                                    )
                                },
                        )
                    if (isModify) {
                        updateSchedule(targetSchedule.scheduleId, newSchedule)
                    } else {
                        createSchedule(newSchedule)
                    }
                },
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

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = if (!isModify) "일정 추가를 종료하시겠어요?" else "일정 수정을 종료하시겠어요?",
                    onConfirmClick = {
                        showDialog = false
                        popUpBackStack()
                    },
                    onDismissClick = { showDialog = false },
                )
            }
        }

        BackHandler(enabled = !showDialog) {
            showDialog = true
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
                .noRippleClickable { onChecked(!isChecked) },
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
            profile = profile,
            size = 35,
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = profile.nickname,
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
                nickname = "홍길동",
                backgroundColor = "0xFFC9D0FF",
            ),
    )
}

@Preview
@Composable
private fun ScheduleCreateScreenPreview() {
    ScheduleCreateScreen(
        state = ScheduleUiState(),
        targetSchedule = Schedule(),
    )
}
