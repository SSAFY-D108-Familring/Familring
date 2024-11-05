package com.familring.presentation.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Typography

@Composable
fun MyPageRoute(
    modifier: Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    MyPageScreen(modifier = modifier, onNavigateBack = onNavigateBack)
}

@Composable
fun MyPageScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
) {

    val showDialog = remember { mutableStateOf(false) }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(text = "마이페이지", style = Typography.headlineMedium.copy(fontSize = 22.sp))
                },
                onNavigationClick = onNavigateBack,
            )
            Spacer(
                modifier = Modifier.fillMaxSize(0.05f),
            )

            Text(modifier = Modifier.padding(16.dp).background(Color.Red).padding(16.dp), text = "기분 수정하기")
        }
    }
}

@Preview
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        modifier = Modifier,
        onNavigateBack = {},
    )
}
