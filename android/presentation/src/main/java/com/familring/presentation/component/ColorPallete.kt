package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

@Composable
fun ColorPalette(
    colors: Map<Color, String>,
    secondColors: Map<Color, String>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            colors.forEach { color ->
                ColorText(
                    color = color.key,
                    text = color.value,
                    isSelected = color.key == selectedColor,
                    onClick = { onColorSelected(color.key) },
                )
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            secondColors.forEach { color ->
                ColorText(
                    color = color.key,
                    text = color.value,
                    isSelected = color.key == selectedColor,
                    onClick = { onColorSelected(color.key) },
                )
            }
        }
    }
}

@Composable
fun ColorText(
    color: Color,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .wrapContentSize()
                .border(
                    width = 1.dp,
                    color = if (color == Color.White) Gray03 else Color.Transparent,
                    shape = RoundedCornerShape(30.dp),
                ).noRippleClickable { onClick() },
    ) {
        Text(
            modifier =
                Modifier
                    .background(color = color, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            text = text,
            color = if (color.luminance() > 0.5f) Black else Color.White,
            style = Typography.displayMedium.copy(fontSize = 15.sp),
        )
        if (isSelected) {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "color_selected",
                    tint = Color.White,
                )
            }
        }
    }
}
