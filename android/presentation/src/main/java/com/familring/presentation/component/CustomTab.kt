package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    selectedItemIndex: Int,
    tabs: List<String>,
    onClick: (index: Int) -> Unit,
) {
    Box(
        modifier =
            modifier
                .background(White)
                .height(intrinsicSize = IntrinsicSize.Min),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            tabs.mapIndexed { index, text ->
                val isSelected = selectedItemIndex == index
                TabItem(
                    isSelected = isSelected,
                    onClick = { onClick(index) },
                    text = text,
                )
            }
        }
    }
}

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    val tabTextStyle =
        if (isSelected) {
            Typography.titleMedium.copy(
                fontSize = 16.sp,
                color = Brown01,
            )
        } else {
            Typography.displayMedium.copy(
                fontSize = 16.sp,
                color = Gray02,
            )
        }

    Column(
        modifier = modifier.width(IntrinsicSize.Max),
    ) {
        Text(
            modifier =
                Modifier
                    .noRippleClickable { onClick() }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 5.dp),
            textAlign = TextAlign.Center,
            text = text,
            style = tabTextStyle,
        )

        if (isSelected) {
            HorizontalDivider(
                color = Brown01,
                thickness = 3.dp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TabItemPreview() {
    TabItem(
        isSelected = true,
        onClick = {},
        text = "test",
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomTabPreview() {
    CustomTab(
        selectedItemIndex = 0,
        tabs = listOf("작성", "목록"),
        onClick = {},
    )
}
