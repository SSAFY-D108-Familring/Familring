package com.familring.presentation.screen.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.DateInputRow
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.isDateFormValid
import java.time.LocalDate

@Composable
fun BirthRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    popUpBackStack: () -> Unit,
    navigateToColor: () -> Unit,
) {
    BirthScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToColor = navigateToColor,
        updateBirth = viewModel::updateBirthDate,
    )
}

@Composable
fun BirthScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToColor: () -> Unit = {},
    updateBirth: (LocalDate) -> Unit = {},
) {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val isButtonEnabled = isDateFormValid(year, month, date)

    val focusManager = LocalFocusManager.current

    LaunchedEffect(year, month, date) {
        val birth = LocalDate.of(year.toInt(), month.toInt(), date.toInt())
        updateBirth(birth)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "생년월일 입력",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "입력해 주시는 생년월일을 바탕으로",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "귀여운 12간지 캐릭터 프로필을 만들어 드려요!",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
            DateInputRow(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally),
                year = year,
                month = month,
                date = date,
                onYearChange = { year = it },
                onMonthChange = { month = it },
                onDateChange = { date = it },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            RoundLongButton(
                text = "다음으로",
                onClick = navigateToColor,
                enabled = isButtonEnabled,
            )
        }
    }
}

@Composable
@Preview
fun BirthScreenPreview() {
    BirthScreen()
}
