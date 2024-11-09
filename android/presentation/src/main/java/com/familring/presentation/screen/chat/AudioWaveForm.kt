package com.familring.presentation.screen.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.familring.presentation.theme.Red01

@Composable
fun AudioWaveForm(
    modifier: Modifier = Modifier,
    amplitudes: List<Int>,
    maxAmplitude: Int = 32767,
    amplitudeMultiplier: Float = 1.5f, // 파형 크기 조절
) {
    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(100.dp),
    ) {
        val width = size.width
        val height = size.height
        val lineWidth = 3.dp.toPx()
        val spacing = 2.dp.toPx()

        val pointsToShow = (width / (lineWidth + spacing)).toInt().coerceAtMost(amplitudes.size)
        val points = amplitudes.takeLast(pointsToShow)

        points.forEachIndexed { index, amplitude ->
            val normalizedAmplitude =
                (amplitude.toFloat() / maxAmplitude).coerceIn(0f, 1f) * amplitudeMultiplier
            val x = index * (lineWidth + spacing)
            val lineHeight = (height * normalizedAmplitude).coerceAtMost(height)

            drawLine(
                color = Red01,
                start = Offset(x, height / 2 - lineHeight / 2),
                end = Offset(x, height / 2 + lineHeight / 2),
                strokeWidth = lineWidth,
            )
        }
    }
}
