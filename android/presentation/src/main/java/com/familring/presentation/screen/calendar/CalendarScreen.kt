package com.familring.presentation.screen.calendar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.calendar.DaySchedule
import com.familring.domain.model.calendar.PreviewDaily
import com.familring.domain.model.calendar.PreviewSchedule
import com.familring.domain.model.calendar.Schedule
import com.familring.presentation.R
import com.familring.presentation.component.IconCustomDropBoxStyles
import com.familring.presentation.component.IconCustomDropdownMenu
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.component.TwoButtonTextDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarRoute(
    modifier: Modifier,
    navigateToCreateSchedule: () -> Unit,
    navigateToModifySchedule: (Schedule) -> Unit,
    navigateToCreateDaily: () -> Unit,
    navigateToCreateAlbum: () -> Unit,
    navigateToAlbum: (Long) -> Unit,
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
        navigateToCreateSchedule = navigateToCreateSchedule,
        navigateToCreateDaily = navigateToCreateDaily,
        navigateToCreateAlbum = navigateToCreateAlbum,
        navigateToAlbum = navigateToAlbum,
        navigateToModifySchedule = navigateToModifySchedule,
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
    navigateToCreateSchedule: () -> Unit = {},
    navigateToCreateDaily: () -> Unit = {},
    navigateToCreateAlbum: () -> Unit = {},
    navigateToAlbum: (Long) -> Unit = {},
    navigateToModifySchedule: (Schedule) -> Unit = {},
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
    var showDialog by remember { mutableStateOf(false) }
    var deleteTargetScheduleId = -1L

    LaunchedEffect(selectedMonth) {
        getMonthData(selectedMonth.year, selectedMonth.monthValue)
    }

    LaunchedEffect(event) {
        event.collect { event ->
            when (event) {
                is CalendarUiEvent.Loading -> {
                    // 로딩 중
                }

                is CalendarUiEvent.DeleteSuccess -> {
                    getMonthData(selectedMonth.year, selectedMonth.monthValue)
                }

                is CalendarUiEvent.Error -> {
                    showSnackBar(event.message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToCreateSchedule() },
                containerColor = Green01,
                contentColor = White,
                shape = CircleShape,
            ) {
                IconCustomDropdownMenu(
                    modifier = Modifier,
                    menuItems =
                        listOf(
                            "일정 생성" to { navigateToCreateSchedule() },
                            "일상 공유" to { navigateToCreateDaily() },
                        ),
                    styles =
                        IconCustomDropBoxStyles(
                            iconSize = 30.dp,
                            iconDrawableId = R.drawable.ic_add,
                            expandedIconDrawableId = R.drawable.ic_add,
                            iconColor = White,
                        ),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
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
                if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) { // 성능 개선 용
                    MonthGrid(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        date = month,
                        daySchedules =
                            createDaySchedules(
                                month,
                                state.previewSchedules,
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
                    showDeleteDialog = {
                        deleteTargetScheduleId = it
                        showDialog = true
                    },
                    navigateToModifySchedule = navigateToModifySchedule,
                    navigateToCreateAlbum = navigateToCreateAlbum,
                    navigateToAlbum = navigateToAlbum,
                )
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = "일정을 삭제하시겠어요?",
                    onConfirmClick = {
                        deleteSchedule(deleteTargetScheduleId)
                        showDialog = false
                    },
                    onDismissClick = { showDialog = false },
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

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        state = CalendarUiState(),
        event = MutableSharedFlow(),
    )
}
