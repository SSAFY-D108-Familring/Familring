package com.familring.presentation.screen.notification

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.notification.NotificationResponse

@Stable
sealed interface NotificationListState {
    @Immutable
    data object Loading : NotificationListState

    @Immutable
    data class Success(
        val notificationList: List<NotificationResponse>,
    ) : NotificationListState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : NotificationListState
}
