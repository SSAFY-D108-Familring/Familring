package com.familring.presentation.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.ChatItem
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.chat.ChatInputBar
import com.familring.presentation.component.chat.MyMessage
import com.familring.presentation.component.chat.OtherMessage
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun ChatRoute(modifier: Modifier) {
    ChatScreen(modifier = modifier)
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
) {
    var inputMessage by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val myId = 1L
    val chatList =
        remember {
            mutableStateListOf(
                ChatItem(
                    userId = 1,
                    message = "잘들 지내시는가",
                    nickname = "나갱이",
                    profileImg = "",
                    color = "0xFF949494",
                ),
                ChatItem(
                    userId = 2,
                    message = "나경이 저녁 먹었니?",
                    nickname = "엄마미",
                    profileImg = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/닭.png",
                    color = "0xFFFFE1E1",
                ),
                ChatItem(
                    userId = 3,
                    message = "밥 잘 챙겨먹구 다녀",
                    nickname = "아빵이",
                    profileImg = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/원숭이.png",
                    color = "0xFFC9D0FF",
                ),
                ChatItem(
                    userId = 3,
                    message = "요즘은 뭐하고 지내 안 심심해?",
                    nickname = "아빵이",
                    profileImg = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/원숭이.png",
                    color = "0xFFC9D0FF",
                ),
            )
        }

    LaunchedEffect(chatList.size) {
        if (chatList.isNotEmpty()) {
            lazyListState.scrollToItem(chatList.size - 1)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "채팅",
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(chatList.size) { index ->
                    val item = chatList[index]
                    if (item.userId == myId) {
                        MyMessage(message = item.message)
                    } else {
                        OtherMessage(
                            nickname = item.nickname,
                            profileImg = item.profileImg,
                            color = item.color,
                            message = item.message,
                        )
                    }
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            WindowInsets.ime.exclude(WindowInsets.navigationBars).asPaddingValues(),
                        ).padding(vertical = 5.dp, horizontal = 10.dp),
            ) {
                ChatInputBar(
                    value = inputMessage,
                    onValueChanged = { inputMessage = it },
                    sendMessage = {
                        chatList.add(ChatItem(userId = 1, message = it))
                        inputMessage = ""
                    },
                )
            }
        }
    }
}
