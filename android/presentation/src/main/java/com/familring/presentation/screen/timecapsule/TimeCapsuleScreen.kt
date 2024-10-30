package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.CustomTab
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun TimeCapsuleRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToCreate: () -> Unit,
) {
    TimeCapsuleScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToCreate = navigateToCreate,
    )
}

@Composable
fun TimeCapsuleScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToCreate: () -> Unit = {},
) {
    val tabs = listOf("작성", "목록")
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "타임 캡슐",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CustomTab(
                selectedItemIndex = selectedItemIndex,
                tabs = tabs,
                onClick = { selectedItemIndex = it },
            )
            when (selectedItemIndex) {
                0 ->
                    WritingTimeCapsuleScreen(
                        writingState = 0,
                        navigateToCreate = navigateToCreate,
                    )

                1 -> TimeCapsuleListScreen()
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleScreenPreview() {
    TimeCapsuleScreen()
}
