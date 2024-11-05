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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.DaySchedule
import com.familring.domain.model.PreviewSchedule
import com.familring.presentation.R
import com.familring.presentation.component.IconCustomDropBoxStyles
import com.familring.presentation.component.IconCustomDropdownMenu
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.toLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarRoute(
    modifier: Modifier,
    navigateToCreateSchedule: () -> Unit,
    navigateToCreateDaily: () -> Unit,
    navigateToCreateAlbum: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by calendarViewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreen(
        modifier = modifier,
        state = uiState,
        getMonthData = calendarViewModel::getMonthData,
        getDaySchedules = calendarViewModel::getDaySchedules,
        deleteSchedule = calendarViewModel::deleteSchedule,
        navigateToCreateSchedule = navigateToCreateSchedule,
        navigateToCreateDaily = navigateToCreateDaily,
        navigateToCreateAlbum = navigateToCreateAlbum,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: CalendarUiState,
    getMonthData: (Int, Int) -> Unit = { _, _ -> },
    getDaySchedules: (List<Long>) -> Unit = {},
    deleteSchedule: (Long) -> Unit = {},
    navigateToCreateSchedule: () -> Unit = {},
    navigateToCreateDaily: () -> Unit = {},
    navigateToCreateAlbum: () -> Unit = {},
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

    LaunchedEffect(selectedMonth) {
        getMonthData(selectedMonth.year, selectedMonth.monthValue)
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
                            ),
                        onDayClick = {
                            selectedDay = it
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
                    schedules = schedules,
                    dailyLifes = dailyLifes,
                    navigateToCreateAlbum = navigateToCreateAlbum,
                )
            }
        }
    }
}

private fun createDaySchedules(
    selectedMonth: LocalDate,
    previewSchedules: List<PreviewSchedule>,
): List<DaySchedule> =
    (1..selectedMonth.lengthOfMonth()).map { day ->
        val date = selectedMonth.withDayOfMonth(day)
        val schedules =
            previewSchedules.filter {
                val startDate = it.startTime.toLocalDate()
                val endDate = it.endTime.toLocalDate()

                date in startDate..endDate
            }
        DaySchedule(date.toString(), schedules)
    }

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        state = CalendarUiState(),
    )
}
