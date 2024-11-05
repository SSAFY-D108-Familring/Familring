package com.familring.presentation.screen.mypage

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
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<MyPageState>(MyPageState.Init)
        val state = _state.asStateFlow()

        fun signOut() {
            viewModelScope.launch {
                userRepository.signOut().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.value = MyPageState.Success
                        }

                        is ApiResponse.Error -> {
                            _state.value =
                                MyPageState.Error(
                                    code = response.code,
                                    message = response.message,
                                )
                        }
                    }
                }
            }
        }
    }
