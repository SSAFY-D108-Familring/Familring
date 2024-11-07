package com.familring.presentation.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography

@Composable
fun NoBorderNumberTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    focusManager: FocusManager,
) {
    OutlinedTextField(
        modifier = modifier.width(50.dp),
        placeholder = {
            if (value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = placeholder,
                        style =
                            Typography.titleLarge.copy(
                                fontSize = 24.sp,
                                letterSpacing = 0.em,
                            ),
                        color = Gray03,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        value = value,
        onValueChange = {
            if (it.length <= 1) {
                onValueChange(it)
            }
        },
        singleLine = true,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
        shape = RoundedCornerShape(12.dp),
        textStyle =
            Typography.titleLarge.copy(
                fontSize = 24.sp,
                letterSpacing = 0.em,
                textAlign = TextAlign.Center,
            ),
        colors =
            TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                unfocusedPlaceholderColor = Gray02,
                focusedPlaceholderColor = Gray02,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun NoBorderTextFieldPreview() {
    var value by remember { mutableStateOf("") }

    NoBorderNumberTextField(
        value = value,
        onValueChange = {
            value = it
        },
        focusManager = LocalFocusManager.current,
        placeholder = "0",
    )
}
