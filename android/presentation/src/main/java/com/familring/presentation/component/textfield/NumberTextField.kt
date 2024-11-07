package com.familring.presentation.component.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun NumberTextField(
    modifier: Modifier = Modifier,
    number: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    focusManager: FocusManager,
    maxLength: Int,
    borderColor: Color = if (number.isEmpty()) Gray03 else Green03,
    textColor: Color = if (number.isEmpty()) Gray03 else Black,
) {
    OutlinedTextField(
        modifier =
            modifier.border(
                width = 3.dp,
                shape = RoundedCornerShape(12.dp),
                color = borderColor,
            ),
        placeholder = {
            if (number.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = placeholder,
                        style =
                            Typography.displayLarge.copy(
                                fontSize = 28.sp,
                            ),
                        color = Gray03,
                    )
                }
            }
        },
        value = number,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        singleLine = true,
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
        shape = RoundedCornerShape(12.dp),
        textStyle =
            Typography.headlineLarge.copy(
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
            ),
        colors =
            TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
            ),
    )
}

@Preview
@Composable
private fun NumberTextFieldPreview() {
    NumberTextField(
        number = "",
        onValueChange = {},
        placeholder = "YYYY",
        focusManager = LocalFocusManager.current,
        maxLength = 4,
    )
}
