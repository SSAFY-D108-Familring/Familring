package com.familring.presentation.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        val userRepository: UserRepository,
    ) : ViewModel() {
        private val _notificationListState =
            MutableStateFlow<NotificationListState>(NotificationListState.Loading)
        val notificationListState = _notificationListState.asStateFlow()

//        init {
//            viewModelScope.launch {
//                getNotificationList()
//            }
//        }

        fun getNotificationList() {
            viewModelScope.launch {
                userRepository.getNotifications().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _notificationListState.value =
                                NotificationListState.Success(response.data)
                        }

                        is ApiResponse.Error -> {
                            _notificationListState.value =
                                NotificationListState.Error(response.message)
                        }
                    }
                }
            }
        }
    }
