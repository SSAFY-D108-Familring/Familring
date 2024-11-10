package com.familring.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
    @Inject
    constructor() : ViewModel() {
        fun connectStomp() {
            viewModelScope.launch {
                val okHttpclient =
                    OkHttpClient
                        .Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            },
                        ).build()

                val client =
                    StompClient(
                        OkHttpWebSocketClient(okHttpclient),
                    )
            }
        }
    }
