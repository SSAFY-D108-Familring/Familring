package com.familring.presentation.screen.calendar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarRoute(modifier: Modifier) {
    CalendarScreen(modifier = modifier)
}

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    val pageCount = 120
    val pagerState =
        rememberPagerState(
            initialPage = pageCount / 2,
            pageCount = { pageCount },
        )
    val coroutineScope = rememberCoroutineScope()

    val today = LocalDate.now()
    val selectedMonth by remember {
        derivedStateOf { today.plusMonths((pagerState.currentPage - pageCount / 2).toLong()) }
    }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Green01,
                contentColor = White,
                shape = CircleShape,
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "ic_add",
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
                        daySchedules = daySchedules, // 나중에 바꿔야 함
                        onDayClick = { selectedDay = it },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen()
}
