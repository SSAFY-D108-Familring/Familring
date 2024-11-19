package com.familring.presentation.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.component.ColorPalette
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Blue01
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Pink01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.Yellow01
import com.familring.presentation.util.toColor
import com.familring.presentation.util.toColorLongString
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditColorRoute(
    modifier: Modifier,
    viewModel: MyPageViewModel,
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MyPageUiEvent.ColorUpdateSuccess -> {
                    showSnackBar("배경색이 변경되었어요")
                    popUpBackStack()
                }

                else -> {}
            }
        }
    }

    when (val uiState = state) {
        is MyPageUiState.Success -> {
            EditColorScreen(
                modifier = modifier,
                popUpBackStack = popUpBackStack,
                userColor = uiState.userColor,
                updateColor = viewModel::updateColor,
            )
        }

        else -> {}
    }
}

@Composable
fun EditColorScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    userColor: String = "",
    updateColor: (String) -> Unit = {},
) {
    var selectedColor by remember { mutableStateOf(userColor.toColor()) }
    val colors =
        mapOf(
            Black to "검정색",
            White to "흰색",
            Gray03 to "회색",
            Brown01 to "갈색",
        )
    val secondColors =
        mapOf(
            Yellow01 to "노란색",
            Green03 to "초록색",
            Pink01 to "분홍색",
            Blue01 to "파란색",
        )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "프로필 배경색 변경",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "원하는 배경색으로 변경해 보세요!",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.06f))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.4f)
                            .aspectRatio(1f)
                            .clip(shape = CircleShape)
                            .background(color = selectedColor)
                            .border(
                                width = 2.dp,
                                color = if (selectedColor == White) Gray03 else Color.Transparent,
                                shape = CircleShape,
                            ),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                ColorPalette(
                    colors = colors,
                    secondColors = secondColors,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                    },
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                RoundLongButton(
                    text = "변경하기",
                    onClick = { updateColor(selectedColor.toColorLongString()) },
                )
            }
        }
    }
}
