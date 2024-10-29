package com.familring.presentation.component

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.familring.domain.Profile

@Composable
fun OverlappingProfileLazyRow(
    modifier: Modifier = Modifier,
    profiles: List<Profile>,
) {
    LazyRow(
        modifier = modifier,
    ) {
        item {
            if (profiles.isNotEmpty()) {
                OverlappingRow {
                    for (profile in profiles) {
                        ZodiacBackgroundProfile(
                            profile = profile,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OverlappingRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val factor = 0.7f

    Layout(
        modifier = modifier,
        content = content,
    ) { measureables, constraints ->
        val placeables = measureables.map { it.measure(constraints) }
        val widthsExceptFirst = placeables.subList(1, placeables.size).sumOf { it.width }
        val firstWidth = placeables.getOrNull(0)?.width ?: 0

        val totalWidth = firstWidth + (widthsExceptFirst * factor).toInt()
        val height = placeables.maxOf { it.height }
        layout(totalWidth, height) {
            var x = 0
            for (placeable in placeables) {
                placeable.placeRelative(x, 0)
                x += (placeable.width * factor).toInt()
            }
        }
    }
}

@Preview
@Composable
private fun OverlappingProfileLazyRowPreview() {
    OverlappingProfileLazyRow(
        profiles =
            listOf(
                Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
                Profile(zodiacImgUrl = "url1", backgroundColor = "#FFE1E1"),
                Profile(zodiacImgUrl = "url1", backgroundColor = "0xFFFEE222"),
            ),
    )
}
