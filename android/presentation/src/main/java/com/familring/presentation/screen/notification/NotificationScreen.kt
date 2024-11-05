package com.familring.presentation.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green07
import com.familring.presentation.theme.Typography

@Composable
fun NotificationRoute(
    modifier: Modifier,
    navigateToHome: () -> Unit,
) {
    NotificationScreen(modifier = modifier, navigateToHome = navigateToHome)
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
) {
    var notificationCnt = 10

    Surface(
        modifier =
            Modifier
                .background(color = Color.White)
                .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = { Text(text = "알림", style = Typography.headlineMedium) },
                onNavigationClick = navigateToHome,
            )
            Spacer(modifier = Modifier.height(23.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(notificationCnt) {
                    NotificationItem()
                }
            }
        }
    }
}

@Composable
fun NotificationItem() {
    Box {
        ElevatedCard(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 21.dp),
            shape = RoundedCornerShape(20.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = Green07,
                ),
        ) {
            Row(
                modifier =
                    Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(22.dp))
                Spacer(
                    modifier =
                        Modifier
                            .width(3.dp)
                            .height(40.dp)
                            .background(color = Green02, shape = RoundedCornerShape(20.dp)),
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "엄마미가 똑똑 두드렸어요 ✊🏻", // 알림 제목
                        style = Typography.headlineSmall.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "랜덤 질문에 답변을 달고 다른 가족의 답을 확인해 보세요", // 알림 내용
                        style = Typography.labelSmall.copy(fontSize = 12.sp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    NotificationScreen()
}
