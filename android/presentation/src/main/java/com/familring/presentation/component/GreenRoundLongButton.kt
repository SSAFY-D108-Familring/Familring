package com.familring.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography

@Composable
fun GreenRoundLongButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onClick,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Green02,
                    contentColor = Color.White,
                    disabledContainerColor = Gray03,
                    disabledContentColor = Color.White,
                ),
            modifier =
                Modifier
                    .fillMaxWidth(0.9f)
                    .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
        ) {
            Text(
                text = text,
                style = Typography.headlineSmall,
            )
        }
    }
}

@Preview
@Composable
private fun LargeButtonPreview() {
    GreenRoundLongButton(
        text = "긴 버튼입니다",
    )
}
