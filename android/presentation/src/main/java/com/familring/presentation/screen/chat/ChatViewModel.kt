package com.familring.presentation.screen.chat

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.datastore.TokenDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.chat.Chat
import com.familring.domain.model.chat.FileUploadRequest
import com.familring.domain.model.chat.SendMessage
import com.familring.domain.model.chat.VoteResponse
import com.familring.domain.repository.FamilyRepository
import com.familring.presentation.BuildConfig
import com.familring.presentation.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
import org.hildan.krossbow.stomp.conversions.convertAndSend
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import timber.log.Timber
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
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
                .add(LocalDateAdapter())
                .add(LocalDateTimeAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
        private lateinit var chatList: List<Chat>

        // 재생 중인 파일
        private var currentPlayer: VoicePlayer? by mutableStateOf(null)
        var currentPath: String? by mutableStateOf(null)

        private val _state = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
        val state = _state.asStateFlow()
        private val _event = MutableSharedFlow<ChatUiEvent>()
        val event = _event.asSharedFlow()

        init {
            loadUserData()
        }

        fun pauseCurrentPlaying() {
            currentPath?.let {
                currentPlayer?.apply {
                    pause()
                }
                currentPlayer = null
                currentPath = null
            }
        }

        fun setCurrentPlayer(
            player: VoicePlayer,
            filePath: String,
        ) {
            currentPlayer = player
            currentPath = filePath
        }

        fun removePlayer() {
            currentPlayer = null
            currentPath = null
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
                            val currentState = _state.value

                            if (currentState is ChatUiState.Success) {
                                _state.value = currentState.copy(chatList = chatList)
                            } else {
                                _state.value = ChatUiState.Success(userId = userId, chatList = chatList)
                            }
                        }

                        is ApiResponse.Error -> {
                            _state.value = ChatUiState.Error(response.message)
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
                            ).callTimeout(Duration.ofMinutes(1))
                            .pingInterval(Duration.ofSeconds(10))
                            .build()

                    val client = StompClient(OkHttpWebSocketClient(okHttpclient))
                    stompSession =
                        client
                            .connect(
                                url = BuildConfig.SOCKET_URL,
                                customStompConnectHeaders = mapOf(X_USER_ID to userId.toString()),
                            ).withMoshi(moshi)

                    subscribeMessages()
                    subscribeReadStatus()
                }
            }
        }

        // 메시지 구독
        private fun subscribeMessages() {
            viewModelScope.launch {
                stompSession
                    .subscribe(StompSubscribeHeaders(destination = "$SUBSCRIBE_URL$familyId"))
                    .collect { message ->
                        val chatMessage = moshi.adapter(Chat::class.java).fromJson(message.bodyAsText)
                        chatMessage?.let {
                            updateChatListWithNewMessage(it)
                        }
                    }
            }
        }

        // 새로운 메시지 업데이트
        private fun updateChatListWithNewMessage(chatMessage: Chat) {
            val currentState = _state.value
            if (currentState is ChatUiState.Success) {
                val updatedChatList = listOf(chatMessage) + currentState.chatList
                _state.value = currentState.copy(chatList = updatedChatList)
            }
        }

        // 읽음 처리 구독
        private fun subscribeReadStatus() {
            viewModelScope.launch {
                stompSession
                    .subscribe(StompSubscribeHeaders(destination = "$SUBSCRIBE_URL$familyId$READ_STATUS_URL"))
                    .collect {
                        if (familyId != null && userId != null) {
                            enterRoom(roomId = familyId!!, userId = userId!!)
                        }
                    }
            }
        }

        fun disconnect() {
            try {
                viewModelScope.launch {
                    stompSession.disconnect()
                    Timber.d("WebSocket disconnected")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun sendMessage(
            context: Context,
            message: String,
        ) {
            viewModelScope.launch {
                stompSession.withMoshi(moshi).convertAndSend(
                    headers = StompSendHeaders(destination = SEND_URL),
                    body =
                        SendMessage(
                            roomId = familyId.toString(),
                            senderId = userId.toString(),
                            content = message,
                            createdAt = LocalDateTime.now().toString(),
                            messageType = context.getString(R.string.message_type),
                        ),
                )
            }
        }

        fun sendVoteMessage(
            context: Context,
            title: String,
        ) {
            viewModelScope.launch {
                stompSession.withMoshi(moshi).convertAndSend(
                    headers = StompSendHeaders(destination = SEND_URL),
                    body =
                        SendMessage(
                            roomId = familyId.toString(),
                            senderId = userId.toString(),
                            content = "",
                            createdAt = LocalDateTime.now().toString(),
                            messageType = context.getString(R.string.vote_type),
                            voteTitle = title,
                        ),
                )
            }
        }

        fun sendVoteResponse(
            context: Context,
            voteId: String,
            responseOfVote: String,
        ) {
            viewModelScope.launch {
                stompSession.withMoshi(moshi).convertAndSend(
                    headers = StompSendHeaders(destination = VOTE_URL),
                    body =
                        VoteResponse(
                            roomId = familyId.toString(),
                            senderId = userId.toString(),
                            voteId = voteId,
                            messageType = context.getString(R.string.vote_response_type),
                            responseOfVote = responseOfVote,
                        ),
                )
            }
        }

        fun uploadVoice(
            context: Context,
            file: File,
        ) {
            viewModelScope.launch {
                familyRepository
                    .uploadVoice(
                        request = FileUploadRequest(roomId = familyId.toString()),
                        voice = file,
                    ).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                sendVoiceMessage(context, response.data)
                            }

                            is ApiResponse.Error -> {
                                _event.emit(ChatUiEvent.Error(response.code, response.message))
                            }
                        }
                    }
            }
        }

        fun sendVoiceMessage(
            context: Context,
            voiceUrl: String,
        ) {
            viewModelScope.launch {
                stompSession.withMoshi(moshi).convertAndSend(
                    headers = StompSendHeaders(destination = SEND_URL),
                    body =
                        SendMessage(
                            roomId = familyId.toString(),
                            senderId = userId.toString(),
                            content = voiceUrl,
                            createdAt = LocalDateTime.now().toString(),
                            messageType = context.getString(R.string.voice_type),
                        ),
                )
            }
        }

        companion object {
            const val X_USER_ID = "X-User-ID"
            const val SUBSCRIBE_URL = "/room/"
            const val READ_STATUS_URL = "/readStatus"
            const val SEND_URL = "/send/chat.send"
            const val VOTE_URL = "/send/chat.vote"
        }
    }
