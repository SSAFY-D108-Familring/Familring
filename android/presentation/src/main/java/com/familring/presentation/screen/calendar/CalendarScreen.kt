package com.familring.presentation.screen.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.time.LocalDate

@Composable
fun CalendarRoute(modifier: Modifier) {
    CalendarScreen(modifier = modifier)
}

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
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
        val today = LocalDate.now()
        var selectedMonth by remember { mutableStateOf<LocalDate>(today) }
        var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

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
                onPrevClick = { selectedMonth = selectedMonth.minusMonths(1) },
                onNextClick = { selectedMonth = selectedMonth.plusMonths(1) },
            )
            MonthGrid(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 5.dp),
                date = selectedMonth,
                daySchedules = daySchedules, // 나중에 바꿔야 함
                onDayClick = { selectedDay = it },
            )
        }
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen()
}
