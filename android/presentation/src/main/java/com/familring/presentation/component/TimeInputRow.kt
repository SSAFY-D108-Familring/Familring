package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.textfield.NumberTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun TimeInputRow(
    modifier: Modifier = Modifier,
    hour: String,
    minute: String,
    isAm: Boolean,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onAmPmChanged: (isAm: Boolean) -> Unit,
    focusManager: FocusManager,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = hour,
            onValueChange = onHourChange,
            placeholder = "09",
            focusManager = focusManager,
            maxLength = 2,
            borderColor = Green02,
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(6.dp)
                        .background(color = Brown01, shape = CircleShape),
            )
            Box(
                modifier =
                    Modifier
                        .size(6.dp)
                        .background(color = Brown01, shape = CircleShape),
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        NumberTextField(
            modifier = Modifier.weight(1f),
            number = minute,
            onValueChange = onMinuteChange,
            placeholder = "00",
            focusManager = focusManager,
            maxLength = 2,
            borderColor = Green02,
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.02f))
        AmPmPicker(
            modifier = Modifier.weight(1f),
            isAm = isAm,
            onAmPmChanged = onAmPmChanged,
        )
    }
}

@Composable
fun AmPmPicker(
    modifier: Modifier = Modifier,
    isAm: Boolean,
    onAmPmChanged: (isAm: Boolean) -> Unit,
) {
    val selectedBackgroundColor = Green02
    val selectedTextColor = White
    val unselectedBackgroundColor = Color.Transparent
    val unselectedTextColor = Black

    Row(
        modifier =
            modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                ).border(
                    width = 3.dp,
                    color = Green02,
                    shape = RoundedCornerShape(12.dp),
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                        color = if (isAm) selectedBackgroundColor else unselectedBackgroundColor,
                    ).noRippleClickable { onAmPmChanged(true) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(5.dp),
                text = "오전",
                style =
                    Typography.displayLarge.copy(
                        color = if (isAm) selectedTextColor else unselectedTextColor,
                        fontSize = 16.sp,
                    ),
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        color = if (!isAm) selectedBackgroundColor else unselectedBackgroundColor,
                    ).noRippleClickable { onAmPmChanged(false) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(5.dp),
                text = "오후",
                style =
                    Typography.displayLarge.copy(
                        color = if (!isAm) selectedTextColor else unselectedTextColor,
                        fontSize = 16.sp,
                    ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeInputRowPreview() {
    TimeInputRow(
        hour = "12",
        minute = "34",
        isAm = false,
        onHourChange = {},
        onMinuteChange = {},
        onAmPmChanged = {},
        focusManager = LocalFocusManager.current,
    )
}
