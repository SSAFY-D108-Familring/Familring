package com.familring.presentation.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography

@Composable
fun NoBorderTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    focusManager: FocusManager,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            if (value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = placeholder,
                        style =
                            Typography.bodyLarge.copy(
                                fontSize = 20.sp,
                            ),
                        color = Gray03,
                    )
                }
            }
        },
        value = value,
        onValueChange = {
            onValueChange(it)
        },
//        keyboardActions =
//            KeyboardActions(onDone = {
//                focusManager.clearFocus()
//            }),
        shape = RoundedCornerShape(12.dp),
        textStyle =
            Typography.bodyLarge.copy(
                fontSize = 20.sp,
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
fun NoBorderPreview() {
    var value by remember { mutableStateOf("") }

    NoBorderTextField(
        value = value,
        onValueChange = {
            value = it
        },
        focusManager = LocalFocusManager.current,
        placeholder = "답변을 입력해 주세요",
    )
}
