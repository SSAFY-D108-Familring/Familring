package com.familring.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    placeHolder: String = "",
    focusManager: FocusManager,
) {
    OutlinedTextField(
        modifier =
            modifier
                .fillMaxWidth()
                .height(55.dp)
                .border(
                    width = 2.dp,
                    color = if (value.isEmpty()) Gray03 else Green03,
                    shape = RoundedCornerShape(12.dp),
                ),
        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        placeholder = {
            if (value.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                    color = Gray03,
                    textAlign = TextAlign.Center,
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors =
            TextFieldDefaults.colors(
                unfocusedIndicatorColor = Gray04,
                focusedIndicatorColor = Gray04,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                unfocusedPlaceholderColor = Gray02,
                focusedPlaceholderColor = Gray02,
            ),
        textStyle = Typography.displayMedium.copy(fontSize = 16.sp),
        keyboardActions =
            KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
    )
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "",
        onValueChanged = {},
        placeHolder = "닉네임을 입력해 주세요",
        focusManager = LocalFocusManager.current,
    )
}
