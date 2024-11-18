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
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green06
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationRoute(
    modifier: Modifier,
    navigateToHome: () -> Unit,
    navigateToQuestion: () -> Unit,
    navigateToSchedule: () -> Unit,
    navigateToChat: () -> Unit,
    navigateToTimeCapsule: () -> Unit,
    navigateToInterest: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val notificationListState = viewModel.notificationListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getNotificationList()
    }

    NotificationScreen(
        modifier = modifier,
        navigateToHome = navigateToHome,
        notificationEvent = viewModel.notificationEvent,
        viewModel = viewModel,
        notificationListState = notificationListState.value,
        onNotificationClick = { notification ->
            viewModel.readNotification(notification.notificationId)
            when (notification.notificationType) {
                "KNOCK" -> navigateToQuestion()
                "MENTION_SCHEDULE" -> navigateToSchedule()
                "MENTION_CHAT" -> navigateToHome()
                "RANDOM" -> navigateToQuestion()
                "TIMECAPSULE" -> navigateToTimeCapsule()
                "INTEREST_PICK", "INTEREST_COMPLETE" -> navigateToInterest()
            }
        },
    )
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    viewModel: NotificationViewModel,
    notificationEvent: SharedFlow<NotificationEvent>,
    notificationListState: NotificationListState,
    onNotificationClick: (NotificationResponse) -> Unit = {},
) {
    LaunchedEffect(notificationEvent) {
        notificationEvent.collectLatest { event ->
            when (event) {
                is NotificationEvent.Loading -> {
                    // 로딩
                }

                is NotificationEvent.Success -> {
                    viewModel.getNotificationList()
                }

                is NotificationEvent.Error -> {
                }
            }
        }
    }
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
                            NotificationItem(
                                notificationListState.notificationList[index],
                                onNotificationClick = onNotificationClick,
                            )
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
fun NotificationItem(
    notification: NotificationResponse,
    onNotificationClick: (NotificationResponse) -> Unit = {},
) {
    Box {
        ElevatedCard(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 21.dp)
                    .noRippleClickable {
                        onNotificationClick(
                            notification,
                        )
                    },
            shape = RoundedCornerShape(20.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        if (notification.notificationIsRead) {
                            Gray03
                        } else {
                            Green06
                        },
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
