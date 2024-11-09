package com.familring.presentation.screen.chat

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun VoiceRecordScreen(
    onDismiss: () -> Unit,
    onRecordingComplete: (String) -> Unit,
) {
    val context = LocalContext.current
    val voiceRecorder = remember { VoiceRecorder(context) }
    var isRecording by remember { mutableStateOf(false) }
    var recordedFilePath by remember { mutableStateOf<String?>(null) }
    var amplitudes by remember { mutableStateOf(listOf<Int>()) }
    var recordingTime by remember { mutableStateOf(0L) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                voiceRecorder.startRecording()
                isRecording = true
            }
        }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingTime = 0L
            while (isRecording) {
                delay(1000)
                recordingTime += 1000L
            }
        } else {
            recordingTime = 0L
        }
    }

    LaunchedEffect(isRecording) {
        while (isRecording) {
            amplitudes = voiceRecorder.getAmplitudes()
            delay(100)
        }
    }

    val formattedTime =
        remember(recordingTime) {
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(recordingTime),
                TimeUnit.MILLISECONDS.toSeconds(recordingTime) % 60,
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 녹음된 파일 있을 때
        if (recordedFilePath != null) {
            Text(
                text = "음성메시지",
                style = Typography.headlineMedium,
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            VoicePlaybackUI(
                filePath = recordedFilePath!!,
                onDelete = {
                    recordedFilePath = null
                },
                onSend = {
                    onRecordingComplete(recordedFilePath!!)
                    onDismiss()
                },
            )
        }
        // 녹음 중일 때
        else if (isRecording) {
            Text(
                text = formattedTime,
                style = Typography.headlineSmall,
                color = Black,
            )
            Spacer(modifier = Modifier.height(15.dp))
            AudioWaveForm(
                amplitudes = amplitudes,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            IconButton(
                onClick = {
                    val file = voiceRecorder.stopRecording()
                    recordedFilePath = file?.absolutePath
                    isRecording = false
                },
                modifier =
                    Modifier
                        .size(50.dp),
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_stop),
                    tint = Red01,
                    contentDescription = "ic_stop",
                )
            }
        }
        // 녹음 시작 전
        else {
            Text(
                text = "음성메시지",
                style = Typography.headlineMedium,
                color = Black,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO,
                        ),
                        -> {
                            voiceRecorder.startRecording()
                            isRecording = true
                        }

                        else -> permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_record),
                    contentDescription = "record",
                    tint = Red01,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun VoicePlaybackUI(
    filePath: String,
    onDelete: () -> Unit,
    onSend: () -> Unit,
) {
    val voicePlayer = remember { VoicePlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var totalDuration by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            totalDuration = voicePlayer.getTotalDuration()
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    if (isPlaying) {
                        voicePlayer.pause()
                        isPlaying = false
                    } else {
                        voicePlayer.playMessage(
                            filePath = filePath,
                            onProgressUpdate = { current ->
                                progress = current / totalDuration.toFloat()
                            },
                            onComplete = {
                                isPlaying = false
                                progress = 0f
                            },
                        )
                        isPlaying = true
                    }
                },
            ) {
                Icon(
                    painter =
                        if (isPlaying) {
                            painterResource(id = R.drawable.ic_pause)
                        } else {
                            painterResource(
                                id = R.drawable.ic_play,
                            )
                        },
                    contentDescription = if (isPlaying) "pause" else "play",
                    tint = Gray01,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            LinearProgressIndicator(
                progress = { progress },
                color = Green02,
                trackColor = Gray03,
                modifier =
                    Modifier
                        .weight(1f)
                        .height(8.dp)
                        .padding(horizontal = 8.dp),
            )
            Row {
                IconButton(onClick = {
                    voicePlayer.stopPlaying()
                    isPlaying = false
                    onDelete()
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Gray01,
                    )
                }
                IconButton(onClick = {
                    voicePlayer.stopPlaying()
                    isPlaying = false
                    onSend()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Send",
                        tint = Green02,
                    )
                }
            }
        }
    }
}
