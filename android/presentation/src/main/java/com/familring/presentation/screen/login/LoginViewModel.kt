package com.familring.presentation.screen.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.TokenDataSource
import com.familring.domain.repository.AuthRepository
import com.familring.domain.request.UserLoginRequest
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val application: Application,
        private val tokenDataSource: TokenDataSource,
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
        val loginState = _loginState.asStateFlow()

        private val _loginEvent = MutableSharedFlow<LoginEvent>()
        val loginEvent = _loginEvent.asSharedFlow()

        fun handleKakaoLogin() {
            viewModelScope.launch {
                try {
                    // 카카오톡으로 로그인
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(application)) {
                        loginWithKakaoTalk()
                    } else {
                        // 카카오계정으로 로그인
                        loginWithKakaoAccount()
                    }
                } catch (e: Exception) {
                    _loginState.value = LoginState.Error("로그인 실패: ${e.message}")
                    _loginEvent.emit(LoginEvent.Error(errorMessage = e.message ?: "알 수 없는 오류 발생"))
                }
            }
        }

        private suspend fun loginWithKakaoTalk() {
            try {
                val token =
                    suspendCancellableCoroutine<OAuthToken?> { continuation ->
                        UserApiClient.instance.loginWithKakaoTalk(application) { token, error ->
                            when {
                                error != null -> {
                                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                        continuation.resume(null) {}
                                    } else {
                                        viewModelScope.launch {
                                            try {
                                                loginWithKakaoAccount()
                                            } catch (e: Exception) {
                                                continuation.resumeWithException(e)
                                            }
                                        }
                                    }
                                }

                                token != null ->
                                    continuation.resume(token) {
                                        Log.d("KakaoLogin", "Login success")
                                    }
                            }
                        }
                    }
                handleLoginSuccess(token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("카카오톡 로그인 실패: ${e.message}")
                _loginEvent.emit(LoginEvent.Error(errorMessage = "카카오톡 로그인 실패 ${e.message}"))
            }
        }

        private suspend fun loginWithKakaoAccount() {
            try {
                val token =
                    suspendCancellableCoroutine<OAuthToken?> { continuation ->
                        UserApiClient.instance.loginWithKakaoAccount(application) { token, error ->
                            when {
                                error != null -> continuation.resumeWithException(error)
                                token != null ->
                                    continuation.resume(token) {
                                        Log.d("KakaoLogin", "Login success")
                                    }
                            }
                        }
                    }
                handleLoginSuccess(token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("카카오계정 로그인 실패: ${e.message}")
                _loginEvent.emit(LoginEvent.Error(errorMessage = "카카오계정 로그인 실패 ${e.message}"))
            }
        }

        private fun handleLoginSuccess(token: OAuthToken?) {
            if (token != null) {
                // 사용자 정보 가져오기
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                        viewModelScope.launch {
                            _loginState.value = LoginState.Error("사용자 정보 요청 실패: ${error.message}")
                            _loginEvent.emit(LoginEvent.Error(errorMessage = "사용자 정보 요청 실패 ${error.message}"))
                        }
                    } else if (user != null) {
                        Log.d(
                            "KakaoLogin",
                            "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}",
                        )

                        viewModelScope.launch {
                            authRepository
                                .login(
                                    UserLoginRequest(
                                        userKaKaoId = user.id.toString(),
                                    ),
                                ).collectLatest { response ->
                                    when (response) {
                                        is ApiResponse.Success -> {
                                            tokenDataSource.saveJwtToken(response.data)
                                            _loginState.value = LoginState.Success(token, user.id)
                                            _loginEvent.emit(LoginEvent.LoginSuccess)
                                        }

                                        is ApiResponse.Error -> {
                                            _loginState.value =
                                                LoginState.Error("로그인 실패: ${response.errorMessage}")
                                            _loginEvent.emit(LoginEvent.Error(errorMessage = "로그인 실패 ${response.errorMessage}"))
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
