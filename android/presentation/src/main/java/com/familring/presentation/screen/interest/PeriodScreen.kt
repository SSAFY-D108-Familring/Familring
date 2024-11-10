package com.familring.presentation.screen.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun PeriodScreen(
    modifier: Modifier = Modifier,
    savePeriod: () -> Unit = {},
) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.img_flash_camera),
                contentDescription = "flash_camera",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                text = "우리 가족은 이날까지 인증할게요",
                style = Typography.headlineMedium,
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
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
            Spacer(modifier = Modifier.weight(1f))
            RoundLongButton(
                text = "인증 기간 지정하기",
                onClick = savePeriod,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PeriodScreenPreview() {
    PeriodScreen()
}
