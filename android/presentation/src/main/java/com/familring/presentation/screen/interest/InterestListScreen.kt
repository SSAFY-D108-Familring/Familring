package com.familring.presentation.screen.interest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun InterestListRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
) {
    InterestListScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun InterestListScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "관심사 공유 목록",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
        }
    }
}
