package com.familring.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.datastore.TokenDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.chat.Chat
import com.familring.domain.repository.FamilyRepository
import com.familring.presentation.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
        private val authDataStore: AuthDataStore,
        private val tokenDataStore: TokenDataStore,
    ) : ViewModel() {
        private var userId: Long? = 0L
        private var familyId: Long? = 0L
        private lateinit var stompSession: StompSession
        private val moshi: Moshi =
            Moshi
                .Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

        private lateinit var newChatMessage: Flow<StompFrame.Message>
        private lateinit var chatList: List<Chat>
        var originalChatList: Flow<PagingData<Chat>>? = null

        private val _state = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
        val state = _state.asStateFlow()
        private val _event = MutableSharedFlow<ChatUiEvent>()
        val event = _event.asSharedFlow()

        init {
            loadUserData()
        }

        private fun loadUserData() {
            viewModelScope.launch {
                val userIdJob = async { authDataStore.getUserId() }
                val familyIdJob = async { authDataStore.getFamilyId() }

                userId = userIdJob.await()
                familyId = familyIdJob.await()

                if (userId != null && familyId != null) {
                    enterRoom(userId = userId!!, roomId = familyId!!)
                    connectStomp()
                }
            }
        }

        private fun enterRoom(
            roomId: Long,
            userId: Long,
        ) {
            viewModelScope.launch {
                familyRepository.enterRoom(roomId, userId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            chatList = response.data
                            _state.value = ChatUiState.Success(userId = userId, chatList = chatList)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(ChatUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        private fun connectStomp() {
            viewModelScope.launch {
                val token = tokenDataStore.getAccessToken()

                if (token != null && familyId != null) {
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

                    stompSession =
                        client
                            .connect(
                                url = BuildConfig.SOCKET_URL,
                                customStompConnectHeaders =
                                    mapOf(
                                        X_USER_ID to userId.toString(),
                                    ),
                            ).withMoshi(moshi)

                    newChatMessage =
                        stompSession.subscribe(
                            StompSubscribeHeaders(
                                destination = "$SUBSCRIBE_URL$familyId",
                            ),
                        )

                    newChatMessage.collect { message ->
                        val chatMessage = moshi.adapter(Chat::class.java).fromJson(message.bodyAsText)
                        when (val state = _state.value) {
                            is ChatUiState.Success -> {
                                if (chatMessage != null) {
//                                    chatList = state.chatList + chatMessage
//                                    _state.value = state.copy(chatList = chatList)
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }

        fun disconnect() {
            try {
                viewModelScope.launch {
                    stompSession.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun sendMessage(message: String) {
            viewModelScope.launch {
            }
        }

        companion object {
            const val X_USER_ID = "X-User-ID"
            const val SUBSCRIBE_URL = "/room/"
        }
    }
