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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.component.CustomTextTab
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun TimeCapsuleRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToCreate: () -> Unit,
    timeCapsuleViewModel: TimeCapsuleViewModel = hiltViewModel(),
) {
    TimeCapsuleScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToCreate = navigateToCreate,
        timeCapsuleViewModel = timeCapsuleViewModel,
    )
}

@Composable
fun TimeCapsuleScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToCreate: () -> Unit = {},
    timeCapsuleViewModel: TimeCapsuleViewModel,
) {
    val uiState by timeCapsuleViewModel.uiState.collectAsStateWithLifecycle()

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
            CustomTextTab(
                selectedItemIndex = selectedItemIndex,
                tabs = tabs,
                onClick = { selectedItemIndex = it },
                spaceSize = 40,
            )
            when (selectedItemIndex) {
                0 -> {
                    timeCapsuleViewModel.getTimeCapsuleStatus()
                }

                1 -> timeCapsuleViewModel.getTimeCapsules()
            }

            when (uiState) {
                is TimeCapsuleUiState.Loading -> {
                    //
                }

                is TimeCapsuleUiState.NoTimeCapsule -> {
                    NoTimeCapsule(navigateToCreate = navigateToCreate)
                }

                is TimeCapsuleUiState.WritingTimeCapsule -> {
                    WritingTimeCapsule()
                }

                is TimeCapsuleUiState.FinishedTimeCapsule -> {
                    FinishedTimeCapsule()
                }

                is TimeCapsuleUiState.TimeCapsuleList -> {
                    TimeCapsuleList()
                }

                is TimeCapsuleUiState.Error -> {
                    //
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleScreenPreview() {
//    TimeCapsuleScreen()
}
