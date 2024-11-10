package com.familring.presentation.component.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography

@Composable
fun ChatInputBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    sendMessage: (String) -> Unit,
) {
    OutlinedTextField(
        modifier =
        modifier
            .fillMaxWidth()
            .height(50.dp),
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            if (value.isEmpty()) {
                Text(
                    text = "메시지 입력",
                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                    color = Gray02,
                    textAlign = TextAlign.Center,
                )
            }
        },
        shape = RoundedCornerShape(30.dp),
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
        textStyle = Typography.displayMedium.copy(fontSize = 16.sp),
        trailingIcon = {
            IconButton(onClick = { sendMessage(value) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = Green02,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ChatInputBarPreview() {
    var text by remember { mutableStateOf("") }
    ChatInputBar(
        value = text,
        onValueChanged = {
            text = it
        },
        sendMessage = {},
    )
}
