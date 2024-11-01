package com.familring.presentation.screen.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datasource.TokenDataSource
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserLoginRequest
import com.familring.presentation.MainActivity
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

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val tokenDataSource: TokenDataSource,
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
        val loginState = _loginState.asStateFlow()

        private val _loginEvent = MutableSharedFlow<LoginEvent>()
        val loginEvent = _loginEvent.asSharedFlow()

        fun handleKakaoLogin(activity: Activity) {
            viewModelScope.launch {
                try {
                    Log.d(TAG, "카카오 로그인 시도")
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                        loginWithKakaoTalk(activity)
                    } else {
                        loginWithKakaoAccount(activity)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "카카오 로그인 실패", e)
                    _loginState.value = LoginState.Error("로그인 실패: ${e.message}")
                    _loginEvent.emit(LoginEvent.Error(errorMessage = e.message ?: "알 수 없는 오류 발생"))
                }
            }
        }

        private suspend fun loginWithKakaoTalk(activity: Activity) {
            try {
                Log.d(TAG, "카카오톡으로 로그인 시도")

                val token =
                    suspendCancellableCoroutine<OAuthToken?> { continuation ->
                        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                            when {
                                error != null -> {
                                    // 카카오톡 로그인 실패 시 카카오 계정으로 로그인 시도
                                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                        Log.d(TAG, "카카오톡 로그인 취소")
                                        continuation.resume(null) {}
                                    } else {
                                        Log.e(TAG, "카카오톡 로그인 실패, 카카오 계정으로 시도", error)
                                        viewModelScope.launch {
                                            try {
                                                loginWithKakaoAccount(activity)
                                            } catch (e: Exception) {
                                                continuation.resumeWithException(e)
                                            }
                                        }
                                    }
                                }

                                token != null ->
                                    continuation.resume(token) {
                                        Log.d(TAG, "카카오톡 로그인 성공")
                                    }
                            }
                        }
                    }
                if (token != null) {
                    handleLoginSuccess(token)
                }
            } catch (e: Exception) {
                Log.e(TAG, "카카오톡 로그인 실패", e)
                // 카카오톡 로그인 실패 시 카카오 계정으로 로그인 시도
                loginWithKakaoAccount(activity)
            }
        }

        private suspend fun loginWithKakaoAccount(activity: Activity) {
            try {
                Log.d(TAG, "카카오 계정으로 로그인 시도")
                val token =
                    suspendCancellableCoroutine<OAuthToken?> { continuation ->
                        UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                            when {
                                error != null -> {
                                    Log.e(TAG, "카카오 계정 로그인 실패", error)
                                    continuation.resumeWithException(error)
                                }

                                token != null ->
                                    continuation.resume(token) {
                                        Log.d(TAG, "카카오 계정 로그인 성공")
                                    }
                            }
                        }
                    }
                handleLoginSuccess(token)
            } catch (e: Exception) {
                Log.e(TAG, "카카오 계정 로그인 실패", e)
                _loginState.value = LoginState.Error("카카오계정 로그인 실패: ${e.message}")
                _loginEvent.emit(LoginEvent.Error(errorMessage = "카카오계정 로그인 실패 ${e.message}"))
            }
        }

        private fun handleLoginSuccess(token: OAuthToken?) {
            if (token != null) {
                Log.d(TAG, "카카오 토큰 발급 성공, 사용자 정보 요청")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                        viewModelScope.launch {
                            _loginState.value = LoginState.Error("사용자 정보 요청 실패: ${error.message}")
                            _loginEvent.emit(LoginEvent.Error(errorMessage = "사용자 정보 요청 실패 ${error.message}"))
                        }
                    } else if (user != null) {
                        Log.d(
                            TAG,
                            "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}",
                        )

                        viewModelScope.launch {
                            try {
                                Log.d(TAG, "서버 로그인 시도")
                                userRepository
                                    .login(
                                        UserLoginRequest(userKakaoId = user.id.toString()),
                                    ).collectLatest { response ->
                                        Log.d(TAG, "서버 응답: $response")
                                        when (response) {
                                            is ApiResponse.Success -> {
                                                Log.d(TAG, "서버 로그인 성공: ${response.data}")
                                                _loginState.value = LoginState.Success(token, user.id)
                                                _loginEvent.emit(LoginEvent.LoginSuccess)
                                            }

                                            is ApiResponse.Error -> {
                                                Log.e(TAG, "서버 로그인 실패: ${response.errorMessage}")
                                                _loginState.value =
                                                    LoginState.NoRegistered("로그인 실패: ${response.errorMessage}")
                                                _loginEvent.emit(
                                                    LoginEvent.Error(
                                                        errorCode = response.errorCode,
                                                        errorMessage = "로그인 실패 ${response.errorMessage}",
                                                    ),
                                                )
                                            }
                                        }
                                    }
                            } catch (e: Exception) {
                                Log.e(TAG, "서버 통신 중 오류 발생", e)
                                _loginState.value = LoginState.Error(e.message ?: "서버 통신 실패")
                                _loginEvent.emit(
                                    LoginEvent.Error(
                                        errorMessage = e.message ?: "서버 통신 실패",
                                    ),
                                )
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "카카오 토큰이 null입니다.")
            }
        }
    }
