package com.familring.presentation.component.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Typography

@Composable
fun DateDivider(
    modifier: Modifier = Modifier,
    date: String,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(0.97f)
                    .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier =
                    Modifier.weight(1f),
                thickness = 0.8.dp,
                color = Gray02,
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = date,
                style = Typography.displaySmall.copy(fontSize = 13.sp),
                color = Gray02,
            )
            HorizontalDivider(
                modifier =
                    Modifier.weight(1f),
                thickness = 0.8.dp,
                color = Gray02,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DateDividerPreview() {
    DateDivider(
        date = "2024-11-11",
    )
}
