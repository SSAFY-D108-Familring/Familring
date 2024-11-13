package com.familring.presentation.screen.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.model.calendar.DaySchedule
import com.familring.domain.model.calendar.PreviewDaily
import com.familring.domain.model.calendar.PreviewSchedule
import com.familring.domain.model.calendar.Schedule
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.PriorityQueue

@Composable
fun CalendarRoute(
    modifier: Modifier,
    navigateToCreateSchedule: () -> Unit,
    navigateToModifySchedule: (Schedule) -> Unit,
    navigateToCreateDaily: () -> Unit,
    navigateToModifyDaily: (DailyLife) -> Unit,
    navigateToCreateAlbum: () -> Unit,
    navigateToAlbum: (Long, Boolean) -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
) {
    val uiState by calendarViewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreen(
        modifier = modifier,
        state = uiState,
        event = calendarViewModel.event,
        getMonthData = calendarViewModel::getMonthData,
        getDaySchedules = calendarViewModel::getDaySchedules,
        getDayDailies = calendarViewModel::getDayDailies,
        deleteSchedule = calendarViewModel::deleteSchedule,
        deleteDaily = calendarViewModel::deleteDaily,
        createAlbum = calendarViewModel::createAlbum,
        navigateToCreateSchedule = navigateToCreateSchedule,
        navigateToCreateDaily = navigateToCreateDaily,
        navigateToCreateAlbum = navigateToCreateAlbum,
        navigateToAlbum = navigateToAlbum,
        navigateToModifySchedule = navigateToModifySchedule,
        navigateToModifyDaily = navigateToModifyDaily,
        showSnackBar = showSnackBar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: CalendarUiState,
    event: SharedFlow<CalendarUiEvent>,
    getMonthData: (Int, Int) -> Unit = { _, _ -> },
    getDaySchedules: (List<Long>) -> Unit = {},
    getDayDailies: (List<Long>) -> Unit = {},
    deleteSchedule: (Long) -> Unit = {},
    deleteDaily: (Long) -> Unit = {},
    createAlbum: (Long, String) -> Unit = { _, _ -> },
    navigateToCreateSchedule: () -> Unit = {},
    navigateToCreateDaily: () -> Unit = {},
    navigateToCreateAlbum: () -> Unit = {},
    navigateToAlbum: (Long, Boolean) -> Unit = { _, _ -> },
    navigateToModifySchedule: (Schedule) -> Unit = {},
    navigateToModifyDaily: (DailyLife) -> Unit = {},
    showSnackBar: (String) -> Unit = {},
) {
    // pager
    val coroutineScope = rememberCoroutineScope()
    val pageCount = 120
    val pagerState =
        rememberPagerState(
            initialPage = pageCount / 2,
            pageCount = { pageCount },
        )

    // date
    val today = LocalDate.now()
    val selectedMonth by remember {
        derivedStateOf { today.plusMonths((pagerState.currentPage - pageCount / 2).toLong()) }
    }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    // bottom sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // dialog
    var showScheduleDeleteDialog by remember { mutableStateOf(false) }
    var showDailyDeleteDialog by remember { mutableStateOf(false) }
    var deleteTargetScheduleId = -1L
    var deleteTargetDailyId = -1L

    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedMonth) {
        getMonthData(selectedMonth.year, selectedMonth.monthValue)
    }

    LaunchedEffect(event) {
        event.collect { event ->
            when (event) {
                is CalendarUiEvent.DeleteSuccess -> {
                    getMonthData(selectedMonth.year, selectedMonth.monthValue)
                }

                is CalendarUiEvent.CreateAlbumSuccess -> {
                    showSnackBar("앨범을 생성했어요")
                    navigateToCreateAlbum()
                }

                is CalendarUiEvent.Error -> {
                    showSnackBar(event.message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                color = Gray01.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text(
                                    text = "일상 공유",
                                    style = Typography.labelSmall,
                                    color = White,
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp,
                                        ),
                                )
                            }
                            FloatingActionButton(
                                onClick = { navigateToCreateDaily() },
                                shape = RoundedCornerShape(50.dp),
                                modifier =
                                    Modifier
                                        .padding(end = 7.dp)
                                        .size(40.dp),
                                containerColor = Green01,
                                elevation =
                                    FloatingActionButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp,
                                    ),
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_add),
                                    contentDescription = "ic_add",
                                    tint = White,
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                color = Gray01.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text(
                                    "일정 생성",
                                    style = Typography.labelSmall,
                                    color = White,
                                    modifier =
                                        Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp,
                                        ),
                                )
                            }
                            FloatingActionButton(
                                onClick = { navigateToCreateSchedule() },
                                shape = RoundedCornerShape(50.dp),
                                modifier =
                                    Modifier
                                        .padding(end = 7.dp)
                                        .size(40.dp),
                                containerColor = Green01,
                                elevation =
                                    FloatingActionButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp,
                                    ),
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_add),
                                    contentDescription = "ic_add",
                                    tint = White,
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.fillMaxSize(0.02f))
                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    shape = RoundedCornerShape(50.dp),
                    containerColor = Green01,
                    modifier = Modifier.size(56.dp),
                    elevation =
                        FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp,
                        ),
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "fab_img",
                        tint = White,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "가족 캘린더",
                        color = Black,
                        style = Typography.titleLarge.copy(fontSize = 30.sp),
                    )
                },
                navigationType = TopAppBarNavigationType.None,
            )
            Spacer(modifier = Modifier.height(10.dp))
            MonthController(
                date = selectedMonth,
                onPrevClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1,
                            animationSpec =
                                spring(
                                    stiffness = Spring.StiffnessLow,
                                ),
                        )
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1,
                            animationSpec =
                                spring(
                                    stiffness = Spring.StiffnessLow,
                                ),
                        )
                    }
                },
            )
            Row(
                modifier =
                    Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 5.dp),
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
            HorizontalPager(
                state = pagerState,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp),
            ) { page ->
                val month = remember(page) { today.plusMonths(page.toLong() - (pageCount / 2)) }
                val orderedPreviewSchedules = calcOrder(state.previewSchedules)
                if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) { // 성능 개선 용
                    MonthGrid(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        date = month,
                        daySchedules =
                            createDaySchedules(
                                month,
                                orderedPreviewSchedules,
                                state.previewDailies,
                            ),
                        onDayClick = { daySchedule ->
                            getDaySchedules(
                                daySchedule.schedules.map { it.id },
                            )
                            getDayDailies(
                                daySchedule.dailies.map { it.id },
                            )
                            selectedDay = daySchedule.date
                            showBottomSheet = true
                        },
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = White,
            ) {
                CalendarTab(
                    schedules = state.detailedSchedule,
                    dailyLifes = state.detailedDailies,
                    createAlbum = createAlbum,
                    showDeleteScheduleDialog = {
                        deleteTargetScheduleId = it
                        showScheduleDeleteDialog = true
                    },
                    showDeleteDailyDialog = {
                        deleteTargetDailyId = it
                        showDailyDeleteDialog = true
                    },
                    navigateToModifySchedule = navigateToModifySchedule,
                    navigateToModifyDaily = navigateToModifyDaily,
                    navigateToAlbum = navigateToAlbum,
                )
            }
        }

        if (showScheduleDeleteDialog) {
            Dialog(
                onDismissRequest = { showScheduleDeleteDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = "일정을 삭제하시겠어요?",
                    onConfirmClick = {
                        deleteSchedule(deleteTargetScheduleId)
                        showScheduleDeleteDialog = false
                    },
                    onDismissClick = { showScheduleDeleteDialog = false },
                )
            }
        }

        if (showDailyDeleteDialog) {
            Dialog(
                onDismissRequest = { showDailyDeleteDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = "게시글을 삭제하시겠어요?",
                    onConfirmClick = {
                        deleteDaily(deleteTargetDailyId)
                        showDailyDeleteDialog = false
                    },
                    onDismissClick = { showDailyDeleteDialog = false },
                )
            }
        }
    }
}

private fun createDaySchedules(
    selectedMonth: LocalDate,
    previewSchedules: List<PreviewSchedule>,
    previewDaily: List<PreviewDaily>,
): List<DaySchedule> =
    (1..selectedMonth.lengthOfMonth()).map { day ->
        val date = selectedMonth.withDayOfMonth(day)
        val schedules =
            previewSchedules.filter {
                val startDate = it.startTime.toLocalDate()
                val endDate = it.endTime.toLocalDate()

                date in startDate..endDate
            }
        val dailies =
            previewDaily.filter {
                date == it.createdAt.toLocalDate()
            }
        DaySchedule(date, schedules, dailies)
    }


private fun calcOrder(previewSchedules: List<PreviewSchedule>): List<PreviewSchedule> {
    val occupyList = MutableList<LocalDate?>(3) { null }

    val pq = PriorityQueue<PreviewSchedule>()
    previewSchedules.forEach(
        pq::add,
    )

    while (pq.isNotEmpty()) {
        val currentSchedule = pq.poll()

        for (i in 0..2) {
            val endTime = occupyList[i]

            if (endTime == null) {
                currentSchedule.order = i
                occupyList[i] = currentSchedule.endTime.toLocalDate()
                break
            }

            if (endTime.isBefore(currentSchedule.startTime.toLocalDate())) {
                currentSchedule.order = i
                occupyList[i] = currentSchedule.endTime.toLocalDate()
                break
            }
        }
    }
    previewSchedules.forEach(::println)
    return previewSchedules
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        state = CalendarUiState(),
        event = MutableSharedFlow(),
    )
}
