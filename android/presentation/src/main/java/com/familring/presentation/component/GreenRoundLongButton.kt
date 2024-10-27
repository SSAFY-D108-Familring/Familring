package com.familring.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.05f))
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
                    .weight(1f),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
        ) {
            Text(
                text = text,
                style = Typography.headlineSmall,
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.05f))
    }
}

@Preview
@Composable
private fun LargeButtonPreview() {
    GreenRoundLongButton(
        text = "긴 버튼입니다",
    )
}
