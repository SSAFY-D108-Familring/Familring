package com.familring.presentation.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.notification.NotificationResponse
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green07
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun NotificationRoute(
    modifier: Modifier,
    navigateToHome: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val notificationListState = viewModel.notificationListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getNotificationList()
    }

    NotificationScreen(
        modifier = modifier,
        navigateToHome = navigateToHome,
        viewModel = viewModel,
        notificationListState = notificationListState.value,
    )
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    viewModel: NotificationViewModel,
    notificationListState: NotificationListState,
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
                        text = "알림",
                        style = Typography.headlineMedium.copy(fontSize = 26.sp),
                    )
                },
                onNavigationClick = navigateToHome,
            )
            Spacer(modifier = Modifier.height(23.dp))
            when (notificationListState) {
                is NotificationListState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Green02)
                    }
                }

                is NotificationListState.Success -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 30.dp),
                    ) {
                        items(notificationListState.notificationList.size) { index ->
                            NotificationItem(notificationListState.notificationList[index])
                        }
                    }
                }

                is NotificationListState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = notificationListState.errorMessage,
                            style = Typography.bodyLarge,
                            color = Color.Red,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationResponse) {
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
                        text = notification.notificationTitle, // 알림 제목
                        style = Typography.headlineSmall.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = notification.notificationMessage, // 알림 내용
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
    NotificationScreen(
        viewModel = hiltViewModel(),
        notificationListState = NotificationListState.Loading,
    )
}
