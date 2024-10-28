package com.familring.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography

@Composable
fun CodeTextField(
    modifier: Modifier = Modifier,
    code: String,
    maxLength: Int = 6,
    onValueChange: (String) -> Unit,
    placeHolder: String = "XXXXXX",
    focusManager: FocusManager,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            if (code.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = placeHolder,
                        style =
                            Typography.headlineMedium.copy(
                                fontSize = 28.sp,
                                letterSpacing = 0.em,
                            ),
                        color = Gray03,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        value = code,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        singleLine = true,
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
        shape = RoundedCornerShape(12.dp),
        textStyle =
            Typography.headlineMedium.copy(
                fontSize = 28.sp,
                letterSpacing = 0.em,
                textAlign = TextAlign.Center,
            ),
        colors =
            TextFieldDefaults.colors(
                unfocusedIndicatorColor = Gray04,
                focusedIndicatorColor = Gray04,
                focusedContainerColor = Gray04,
                unfocusedContainerColor = Gray04,
                errorContainerColor = Gray04,
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                unfocusedPlaceholderColor = Gray02,
                focusedPlaceholderColor = Gray02,
            ),
    )
}

@Preview
@Composable
fun CodeTextFieldPreview() {
    CodeTextField(
        code = "",
        onValueChange = {},
        focusManager = LocalFocusManager.current,
    )
}
