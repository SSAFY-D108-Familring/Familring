package com.familring.presentation.screen.signup

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.ColorPalette
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Blue01
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Pink01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.Yellow01
import com.familring.presentation.util.toColorLongString

@Composable
fun ProfileColorRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    popUpBackStack: () -> Unit,
    navigateToNickname: () -> Unit,
) {
    ProfileColorScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToNickname = navigateToNickname,
        updateColor = viewModel::updateColor,
    )
}

@Composable
fun ProfileColorScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToNickname: () -> Unit = {},
    updateColor: (String) -> Unit = {},
) {
    var selectedColor by remember { mutableStateOf(Black) }
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
    
    LaunchedEffect(selectedColor) {
        updateColor(selectedColor.toColorLongString())
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "프로필 배경색 설정",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "좋아하는 배경색을 설정해 보세요",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "12간지 프로필로 만들어 드릴게요!",
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
                    text = "다음으로",
                    onClick = navigateToNickname,
                )
            }
        }
    }
}

@Composable
@Preview
fun ProfileColorScreenPreview() {
    ProfileColorScreen()
}
