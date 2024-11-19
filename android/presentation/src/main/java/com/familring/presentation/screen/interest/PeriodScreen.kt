package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.DateInputRow
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.isDateFormValid
import java.time.LocalDate

@Composable
fun PeriodScreen(
    modifier: Modifier = Modifier,
    setPeriod: (LocalDate) -> Unit = {},
) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // 키보드 높이 감지
    val imeInsets = WindowInsets.ime.exclude(WindowInsets.navigationBars)
    val imeHeight = imeInsets.getBottom(LocalDensity.current)

    LaunchedEffect(imeHeight) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }.verticalScroll(state = scrollState)
                .padding(bottom = with(LocalDensity.current) { (imeHeight).toDp() }),
    ) {
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "관심사를 인증할 수 있는",
            style = Typography.bodyLarge.copy(fontSize = 22.sp),
            color = Black,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "기간을 지정",
                style = Typography.titleSmall.copy(fontSize = 22.sp),
                color = Green03,
            )
            Text(
                text = "해 주세요",
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.img_flash_camera),
                contentDescription = "flash_camera",
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "우리 가족은 이날까지 인증할게요",
                style = Typography.headlineMedium,
                color = Black,
            )
            Spacer(modifier = Modifier.height(20.dp))
            DateInputRow(
                modifier = Modifier.fillMaxWidth(0.9f),
                year = year,
                month = month,
                date = date,
                onYearChange = { year = it },
                onMonthChange = { month = it },
                onDateChange = { date = it },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(15.dp))
            RoundLongButton(
                text = "인증 기간 지정하기",
                onClick = {
                    setPeriod(LocalDate.of(year.toInt(), month.toInt(), date.toInt()))
                },
                enabled =
                    isDateFormValid(year, month, date) &&
                        LocalDate
                            .of(
                                year.toInt(),
                                month.toInt(),
                                date.toInt(),
                            ).isAfter(LocalDate.now()),
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PeriodScreenPreview() {
    PeriodScreen()
}
