package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography

@Composable
fun BrownRoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    val backgroundColor = if (enabled) Brown01 else Gray04
    val textColor = if (enabled) Color.White else Gray03
    Text(
        modifier =
        modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(30.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { onClick() },
        text = text,
        color = textColor,
        style = Typography.displayMedium.copy(fontSize = 15.sp),
    )
}

@Preview
@Composable
fun BrownRoundButtonPreview() {
    BrownRoundButton(onClick = {}, text = "테스트", enabled = false)
}
