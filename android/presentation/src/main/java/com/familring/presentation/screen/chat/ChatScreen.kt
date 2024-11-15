package com.familring.presentation.screen.chat

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.familring.domain.model.chat.Chat
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TutorialScreen
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.chat.ChatInputBar
import com.familring.presentation.component.chat.DateDivider
import com.familring.presentation.component.chat.MyMessage
import com.familring.presentation.component.chat.OtherMessage
import com.familring.presentation.component.chat.VoiceMessage
import com.familring.presentation.component.chat.VoteChatItem
import com.familring.presentation.component.chat.VoteMessage
import com.familring.presentation.component.chat.VoteResultMessage
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.component.textfield.CustomTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green04
import com.familring.presentation.theme.Green05
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toDateOnly
import com.familring.presentation.util.toTimeOnly
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoute(
    modifier: Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val tutorialUiState by viewModel.tutorialUiState.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val chatList = viewModel.chatPagingData.collectAsLazyPagingItems()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTutorial by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ChatUiEvent.Error -> {
                    showSnackBar(event.message)
                }

                else -> {}
            }
        }
    }

    when (val uiState = state) {
        is ChatUiState.Loading -> {
            LoadingDialog(loadingMessage = "이전 대화 내용을 불러오고 있어요...")
        }

        is ChatUiState.Success -> {
            ChatScreen(
                modifier = modifier,
                chatList = chatList,
                userId = viewModel.userId ?: 0,
                popUpBackStack = popUpBackStack,
                showTutorial = {
                    showTutorial = true
                    viewModel.setReadTutorialState(false)
                },
                showSnackBar = showSnackBar,
                sendMessage = viewModel::sendMessage,
                sendVoteMessage = viewModel::sendVoteMessage,
                sendVoteResponse = viewModel::sendVoteResponse,
                sendVoiceMessage = viewModel::uploadVoice,
                pauseCurrentPlaying = viewModel::pauseCurrentPlaying,
                setCurrentPlayer = viewModel::setCurrentPlayer,
                currentPath = viewModel.currentPath,
                removePlayer = viewModel::removePlayer,
            )

            if (showTutorial && !tutorialUiState.isReadTutorial) {
                ModalBottomSheet(
                    containerColor = White,
                    onDismissRequest = {
                        showTutorial = false
                        viewModel.setReadTutorial()
                    },
                    sheetState = sheetState,
                ) {
                    TutorialScreen(
                        imageLists =
                            listOf(
                                R.drawable.img_tutorial_chat_first,
                                R.drawable.img_tutorial_chat_second,
                                R.drawable.img_tutorial_chat_third,
                                R.drawable.img_tutorial_chat_fourth,
                            ),
                        title = "채팅 미리보기 \uD83D\uDD0D",
                        subTitle =
                            "가족들과 채팅은 물론 소소한 투표,\n" +
                                "음성 메시지를 보내며 소통할 수 있어요!",
                    )
                }
            }
        }

        is ChatUiState.Error -> {
            showSnackBar(uiState.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    chatList: LazyPagingItems<Chat>,
    userId: Long = 0L,
    showTutorial: () -> Unit = {},
    sendMessage: (Context, String) -> Unit = { _, _ -> },
    sendVoteMessage: (Context, String) -> Unit = { _, _ -> },
    sendVoteResponse: (Context, String, String) -> Unit = { _, _, _ -> },
    sendVoiceMessage: (Context, File) -> Unit = { _, _ -> },
    pauseCurrentPlaying: () -> Unit = {},
    setCurrentPlayer: (VoicePlayer, String) -> Unit = { _, _ -> },
    currentPath: String? = null,
    removePlayer: () -> Unit = {},
) {
    var inputMessage by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        if (chatList.itemCount > 0) {
            lazyListState.animateScrollToItem(0)
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }.background(color = White),
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
                tutorialIcon = {
                    Icon(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .border(
                                    width = 2.dp,
                                    color = Gray03,
                                    shape = CircleShape,
                                ).padding(2.dp)
                                .noRippleClickable { showTutorial() },
                        painter = painterResource(id = R.drawable.ic_tutorial),
                        contentDescription = "ic_question",
                        tint = Gray03,
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = true,
            ) {
                items(chatList.itemCount) { index ->
                    val listItem = chatList[index]
                    listItem?.let { item ->
                        when (item.messageType) {
                            context.getString(R.string.message_type) -> {
                                if (item.senderId == userId) {
                                    MyMessage(
                                        message = item.content!!,
                                        time = item.createdAt.toTimeOnly(),
                                        unReadMembers = item.unReadMembers.toString(),
                                    )
                                } else {
                                    OtherMessage(
                                        nickname = item.sender.userNickname,
                                        profileImg = item.sender.userZodiacSign,
                                        color = item.sender.userColor,
                                        message = item.content!!,
                                        time = item.createdAt.toTimeOnly(),
                                        unReadMembers = item.unReadMembers.toString(),
                                    )
                                }
                            }

                            context.getString(R.string.vote_type) -> {
                                if (item.senderId == userId) {
                                    item.vote?.let {
                                        VoteMessage(
                                            isOther = false,
                                            title = it.voteTitle,
                                            unReadMembers = item.unReadMembers.toString(),
                                            time = item.createdAt.toTimeOnly(),
                                        )
                                    }
                                } else {
                                    item.vote?.let {
                                        VoteMessage(
                                            isOther = true,
                                            title = it.voteTitle,
                                            onAgree = {
                                                sendVoteResponse(
                                                    context,
                                                    it.voteId,
                                                    "찬성",
                                                )
                                            },
                                            onDisagree = {
                                                sendVoteResponse(
                                                    context,
                                                    it.voteId,
                                                    "반대",
                                                )
                                            },
                                            unReadMembers = item.unReadMembers.toString(),
                                            time = item.createdAt.toTimeOnly(),
                                            nickname = item.sender.userNickname,
                                            profileImg = item.sender.userZodiacSign,
                                            color = item.sender.userColor,
                                        )
                                    }
                                }
                            }

                            context.getString(R.string.vote_response_type) -> {
                                item.vote?.let {
                                    VoteChatItem(
                                        isOther = item.senderId != userId,
                                        title = it.voteTitle,
                                        select = item.responseOfVote,
                                        unReadMembers = item.unReadMembers.toString(),
                                        time = item.createdAt.toTimeOnly(),
                                        nickname = item.sender.userNickname,
                                        profileImg = item.sender.userZodiacSign,
                                        color = item.sender.userColor,
                                    )
                                }
                            }

                            context.getString(R.string.vote_result_type) -> {
                                item.vote?.let {
                                    VoteResultMessage(
                                        isOther = item.senderId != userId,
                                        title = it.voteTitle,
                                        voteResult = it.voteResult,
                                        unReadMembers = item.unReadMembers.toString(),
                                        time = item.createdAt.toTimeOnly(),
                                        nickname = item.sender.userNickname,
                                        profileImg = item.sender.userZodiacSign,
                                        color = item.sender.userColor,
                                    )
                                }
                            }

                            context.getString(R.string.voice_type) -> {
                                item.content?.let {
                                    VoiceMessage(
                                        isOther = item.senderId != userId,
                                        filePath = it,
                                        time = item.createdAt.toTimeOnly(),
                                        unReadMembers = item.unReadMembers.toString(),
                                        nickname = item.sender.userNickname,
                                        profileImg = item.sender.userZodiacSign,
                                        color = item.sender.userColor,
                                        pauseCurrentPlaying = pauseCurrentPlaying,
                                        setCurrentPlayer = setCurrentPlayer,
                                        currentPath = currentPath,
                                        removePlayer = removePlayer,
                                    )
                                }
                            }
                        }
                        val currentDate = item.createdAt.toDateOnly()

                        // 마지막 메시지이거나, 다음 메시지와 날짜가 다르면 날짜 구분선 추가
                        if (index == chatList.itemCount - 1 ||
                            currentDate != chatList[index + 1]?.createdAt?.toDateOnly()
                        ) {
                            DateDivider(date = currentDate)
                        }
                    }
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            WindowInsets.ime
                                .exclude(WindowInsets.navigationBars)
                                .asPaddingValues(),
                        ).padding(vertical = 5.dp)
                        .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { showBottomSheet = true },
                    content = {
                        Icon(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.8f)
                                    .aspectRatio(1f),
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "add",
                            tint = Green02,
                        )
                    },
                )
                ChatInputBar(
                    value = inputMessage,
                    onValueChanged = {
                        inputMessage = it
                    },
                    sendMessage = {
                        sendMessage(context, inputMessage)
                        inputMessage = ""
                    },
                    enabled = inputMessage.isNotBlank(),
                )
            }
        }
    }
    if (showBottomSheet) {
        var clickedItem by remember { mutableStateOf("") }
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = White,
        ) {
            when (clickedItem) {
                "vote" -> {
                    var title by remember { mutableStateOf("") }

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.45f),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "찬반투표 만들기",
                                style = Typography.headlineMedium,
                                color = Black,
                            )
                            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                value = title,
                                onValueChanged = {
                                    title = it
                                },
                                placeHolder = "제목을 작성해 주세요",
                                borderColor = Brown01,
                                focusManager = LocalFocusManager.current,
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            RoundLongButton(
                                text = "투표 올리기",
                                backgroundColor = Brown01,
                                onClick = {
                                    sendVoteMessage(context, title)
                                    showBottomSheet = false
                                },
                                enabled = title.isNotBlank(),
                            )
                        }
                    }
                }

                "voice" -> {
                    VoiceRecordScreen(
                        onDismiss = { showBottomSheet = false },
                        onRecordingComplete = sendVoiceMessage,
                        showSnackBar = showSnackBar,
                        popUpBackStack = { clickedItem = "" },
                    )
                }

                else -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .wrapContentSize()
                                    .padding(bottom = 35.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .wrapContentSize()
                                        .noRippleClickable {
                                            // 찬반투표 클릭 시
                                            clickedItem = "vote"
                                        },
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .clip(shape = CircleShape)
                                            .background(color = Green05)
                                            .padding(15.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_vote),
                                        contentDescription = "vote",
                                        tint = Black,
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "찬반투표",
                                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                                    color = Black,
                                )
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            Column(
                                modifier =
                                    Modifier
                                        .wrapContentSize()
                                        .noRippleClickable {
                                            // 음성메시지 클릭 시
                                            clickedItem = "voice"
                                        },
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .clip(shape = CircleShape)
                                            .background(color = Green04)
                                            .padding(15.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_voice),
                                        contentDescription = "voice_message",
                                        tint = Black,
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "음성메시지",
                                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                                    color = Black,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
