package com.familring.presentation.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.presentation.R
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.screen.chat.VoicePlayer
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.Yellow01

@Composable
fun VoiceMessage(
    isOther: Boolean,
    filePath: String,
    time: String,
    unReadMembers: String,
    nickname: String = "",
    profileImg: String = "",
    color: String = "",
    pauseCurrentPlaying: () -> Unit = {},
    setCurrentPlayer: (VoicePlayer, String) -> Unit = { _, _ -> },
    currentPath: String? = null,
    removePlayer: () -> Unit = {},
) {
    if (!isOther) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Column {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 2.dp),
                            text = if (unReadMembers != "0") unReadMembers else "",
                            style = Typography.titleSmall.copy(fontSize = 10.sp),
                            color = Green02,
                            textAlign = TextAlign.End,
                        )
                        Text(
                            text = time,
                            style = Typography.bodySmall.copy(fontSize = 12.sp),
                            color = Gray02,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(7.dp))
                MessagePlayerUI(
                    filePath = filePath,
                    width = 0.6f,
                    iconColor = Brown01,
                    backgroundColor = Yellow01,
                    lineColor = Brown01,
                    trackColor = Gray03,
                    topStart = 15,
                    bottomEnd = 0,
                    pauseCurrentPlaying = pauseCurrentPlaying,
                    setCurrentPlayer = setCurrentPlayer,
                    currentPath = currentPath,
                    removePlayer = removePlayer,
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
            ) {
                ZodiacBackgroundProfile(
                    modifier = Modifier.padding(top = 5.dp),
                    profile =
                        Profile(
                            nickname = nickname,
                            zodiacImgUrl = profileImg,
                            backgroundColor = color,
                        ),
                    size = 36,
                    paddingValue = 5,
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column {
                    Text(
                        text = nickname,
                        style = Typography.headlineSmall.copy(fontSize = 13.sp),
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    MessagePlayerUI(
                        filePath = filePath,
                        width = 0.65f,
                        iconColor = Yellow01,
                        backgroundColor = Brown01,
                        lineColor = Yellow01,
                        trackColor = Gray01,
                        topStart = 0,
                        bottomEnd = 15,
                        pauseCurrentPlaying = pauseCurrentPlaying,
                        setCurrentPlayer = setCurrentPlayer,
                        currentPath = currentPath,
                        removePlayer = removePlayer,
                    )
                }
                Spacer(modifier = Modifier.width(7.dp))
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 2.dp),
                            text = if (unReadMembers != "0") unReadMembers else "",
                            style = Typography.titleSmall.copy(fontSize = 10.sp),
                            color = Green02,
                        )
                        Text(
                            text = time,
                            style = Typography.bodySmall.copy(fontSize = 12.sp),
                            color = Gray02,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessagePlayerUI(
    filePath: String,
    width: Float,
    iconColor: Color,
    backgroundColor: Color,
    lineColor: Color,
    trackColor: Color,
    topStart: Int,
    bottomEnd: Int,
    pauseCurrentPlaying: () -> Unit,
    setCurrentPlayer: (VoicePlayer, String) -> Unit,
    currentPath: String? = null,
    removePlayer: () -> Unit = {},
) {
    val voicePlayer = remember { VoicePlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(currentPath) {
        if (currentPath != filePath) {
            isPlaying = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            voicePlayer.stopPlaying()
            removePlayer()
        }
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth(width)
                .background(
                    color = backgroundColor,
                    shape =
                        RoundedCornerShape(
                            topStart = topStart.dp,
                            topEnd = 15.dp,
                            bottomStart = 15.dp,
                            bottomEnd = bottomEnd.dp,
                        ),
                ).padding(horizontal = 13.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                if (isPlaying) {
                    voicePlayer.pause()
                    isPlaying = false
                } else {
                    if (currentPath != filePath) {
                        pauseCurrentPlaying()
                        progress = 0f
                    }
                    isPlaying = true
                    setCurrentPlayer(voicePlayer, filePath)
                    voicePlayer.playMessage(
                        filePath = filePath,
                        onProgressUpdate = { current, total ->
                            progress = current / total.toFloat()
                        },
                        onComplete = {
                            isPlaying = false
                            progress = 0f
                            if (currentPath == filePath) {
                                removePlayer()
                            }
                        },
                        onError = {
                        },
                    )
                }
            },
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(0.7f),
                painter =
                    if (isPlaying) {
                        painterResource(id = R.drawable.ic_pause)
                    } else {
                        painterResource(
                            id = R.drawable.ic_play,
                        )
                    },
                contentDescription = if (isPlaying) "pause" else "play",
                tint = iconColor,
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            color = lineColor,
            trackColor = trackColor,
            modifier =
                Modifier
                    .weight(1f)
                    .height(8.dp)
                    .padding(horizontal = 8.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun VoiceMessagePreview() {
    VoiceMessage(
        isOther = true,
        filePath = "",
        time = "13:00",
        unReadMembers = "3",
        nickname = "나갱",
        profileImg = "",
        color = "0xFFD9D9D9",
    )
}
