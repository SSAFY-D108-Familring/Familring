package com.familring.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import com.familring.presentation.component.textfield.NumberTextField
import com.familring.presentation.theme.Green03

@Composable
fun DateInputRow(
    modifier: Modifier = Modifier,
    year: String,
    month: String,
    date: String,
    onYearChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    focusManager: FocusManager,
    borderColor: Color = Green03,
) {
    // FocusRequester 생성
    val yearFocusRequester = remember { FocusRequester() }
    val monthFocusRequester = remember { FocusRequester() }
    val dateFocusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        NumberTextField(
            modifier =
                Modifier
                    .weight(1.5f)
                    .focusRequester(yearFocusRequester),
            number = year,
            onValueChange = onYearChange,
            placeholder = "YYYY",
            focusManager = focusManager,
            maxLength = 4,
            borderColor = borderColor,
            imeAction = ImeAction.Next,
            onNext = { monthFocusRequester.requestFocus() },
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier =
                Modifier
                    .weight(1f)
                    .focusRequester(monthFocusRequester),
            number = month,
            onValueChange = onMonthChange,
            placeholder = "MM",
            focusManager = focusManager,
            maxLength = 2,
            borderColor = borderColor,
            imeAction = ImeAction.Next,
            onNext = { dateFocusRequester.requestFocus() },
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier =
                Modifier
                    .weight(1f)
                    .focusRequester(dateFocusRequester),
            number = date,
            onValueChange = onDateChange,
            placeholder = "DD",
            focusManager = focusManager,
            maxLength = 2,
            borderColor = borderColor,
            imeAction = ImeAction.Done,
            onDone = { focusManager.clearFocus() },
        )
    }
}
