package com.familring.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager

@Composable
fun DateInputRow(
    year: String,
    month: String,
    date: String,
    onYearChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.07f))
        NumberTextField(
            modifier = Modifier.weight(1.5f),
            number = year,
            onValueChange = onYearChange,
            placeholder = "YYYY",
            focusManager = focusManager,
            maxLength = 4,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = month,
            onValueChange = onMonthChange,
            placeholder = "MM",
            focusManager = focusManager,
            maxLength = 2,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = date,
            onValueChange = onDateChange,
            placeholder = "DD",
            focusManager = focusManager,
            maxLength = 2,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.07f))
    }
}
