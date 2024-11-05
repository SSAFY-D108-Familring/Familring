package com.familring.presentation.screen.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _event = MutableSharedFlow<MyPageState>()
        val event = _event.asSharedFlow()

        fun signOut() {
            viewModelScope.launch {
                userRepository.signOut().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _event.emit(MyPageState.Success)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(MyPageState.Error(response.code, response.message))
                        }
                    }
                }
            }
        }
    }
