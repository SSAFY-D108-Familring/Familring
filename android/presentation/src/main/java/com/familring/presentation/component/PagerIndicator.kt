package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    indicatorSize: Int = 10,
    selectedIndicatorColor: Color = Green02,
) {
    Row(
        modifier =
            modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) selectedIndicatorColor else Gray03
            Box(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .size(indicatorSize.dp)
                        .background(color),
            )
        }
    }
}
