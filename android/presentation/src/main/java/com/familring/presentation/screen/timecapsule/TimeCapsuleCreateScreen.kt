package com.familring.presentation.screen.timecapsule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.DateInputRow
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.isDateFormValid
import java.time.LocalDate

@Composable
fun TimeCapsuleCreateRoute(
    modifier: Modifier = Modifier,
    timeCapsuleCreateViewModel: TimeCapsuleCreateViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    showSnackbar: (String) -> Unit,
) {
    val uiState by timeCapsuleCreateViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        timeCapsuleCreateViewModel.event.collect { event ->
            when (event) {
                is TimeCapsuleCreateUiEvent.Success -> {
                    popUpBackStack()
                }

                is TimeCapsuleCreateUiEvent.Error -> {
                    showSnackbar(event.message)
                }
            }
        }
    }

    TimeCapsuleCreateScreen(
        modifier = modifier,
        createTimeCapsule = timeCapsuleCreateViewModel::createTimeCapsule,
        popUpBackStack = popUpBackStack,
    )

    if (uiState.loading) {
        LoadingDialog()
    }
}

@Composable
fun TimeCapsuleCreateScreen(
    modifier: Modifier = Modifier,
    createTimeCapsule: (LocalDate) -> Unit = {},
    popUpBackStack: () -> Unit = {},
) {
    val today = LocalDate.now()
    var year by remember { mutableStateOf(today.year.toString()) }
    var month by remember { mutableStateOf(String.format("%02d", today.monthValue)) }
    var date by remember { mutableStateOf(String.format("%02d", today.plusDays(1).dayOfMonth)) }

    val isDateFormValid by remember { derivedStateOf { isDateFormValid(year, month, date) } }
    val isAfterOneDay by remember { derivedStateOf { isAfterOneDay(year, month, date) } }
    val isButtonEnabled = isDateFormValid && isAfterOneDay

    val errorText =
        if (!isDateFormValid) {
            "날짜 형식을 확인해 주세요"
        } else if (!isAfterOneDay) {
            "타임캡슐은 최소 하루 뒤에 열 수 있어요"
        } else {
            ""
        }

    val focusManager = LocalFocusManager.current

    var showDialog by remember { mutableStateOf(false) }

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
                        text = "타임 캡슐 작성",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = { showDialog = true },
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.img_wrapped_gift),
                contentDescription = "wrapped_gift",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "타임캡슐 오픈일을 지정해 주세요!",
                style = Typography.headlineLarge.copy(fontSize = 26.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "미래의 가족이 열어볼 타임캡슐을 준비해 보세요",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "* 타임캡슐은 최소 하루 뒤에 열 수 있어요",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 16.sp,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 5.dp, bottom = 5.dp),
                text = errorText,
                style =
                    Typography.bodySmall.copy(
                        color = Red01,
                        fontSize = 14.sp,
                    ),
            )
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
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            RoundLongButton(
                text = "타임캡슐 작성하러 가기",
                onClick = {
                    val openDate = LocalDate.of(year.toInt(), month.toInt(), date.toInt())
                    createTimeCapsule(openDate)
                },
                enabled = isButtonEnabled,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            TwoButtonTextDialog(
                text = "타임 캡슐 생성을 종료하시겠어요?",
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

private fun isAfterOneDay(
    year: String,
    month: String,
    date: String,
): Boolean {
    try {
        val today = LocalDate.now()
        val targetDate = LocalDate.of(year.toInt(), month.toInt(), date.toInt())
        val threeDaysLater = today.plusDays(1)

        return targetDate.isAfter(threeDaysLater) || targetDate.isEqual(threeDaysLater)
    } catch (e: Exception) {
        return false
    }
}

@Preview
@Composable
private fun TimeCapsuleCreateScreenPreview() {
    TimeCapsuleCreateScreen()
}
