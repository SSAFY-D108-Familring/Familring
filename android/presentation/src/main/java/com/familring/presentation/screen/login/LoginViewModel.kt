package com.familring.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datasource.AuthDataSource
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserLoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
        val loginState = _loginState.asStateFlow()

        fun login(kakaoId: String) {
            viewModelScope.launch {
                val userLoginRequest = UserLoginRequest(kakaoId)
                userRepository.login(userLoginRequest).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            Log.d("nakyung", authDataSource.getKakaoId()!!)
                            _loginState.value =
                                LoginState.Success(
                                    accessToken = response.data.accessToken,
                                    refreshToken = response.data.refreshToken,
                                )
                        }

                        is ApiResponse.Error -> {
                            _loginState.value =
                                LoginState.Error(
                                    errorMessage = response.message,
                                )
                        }
                    }
                }
            }
        }
    }
