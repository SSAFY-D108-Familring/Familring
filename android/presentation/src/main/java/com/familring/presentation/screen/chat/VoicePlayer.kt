package com.familring.presentation.screen.chat

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import timber.log.Timber

class VoicePlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var isInitialized = false
    private var playing = false
    private var currentMessageUrl: String? = null
    private var pausePosition = 0

    fun playMessage(
        filePath: String,
        onProgressUpdate: (Int) -> Unit,
        onComplete: () -> Unit,
    ) {
        if (mediaPlayer != null) {
            mediaPlayer?.let {
                it.seekTo(pausePosition)
                it.start()
                val handler = Handler(Looper.getMainLooper())
                handler.post(
                    object : Runnable {
                        override fun run() {
                            if (it.isPlaying) {
                                onProgressUpdate(it.currentPosition)
                                handler.postDelayed(this, 50)
                            }
                        }
                    },
                )
            }
            playing = true
        } else {
            currentMessageUrl = filePath
            mediaPlayer =
                MediaPlayer().apply {
                    setDataSource(filePath)
                    setOnPreparedListener { mp ->
                        isInitialized = true
                        mp.start()
                        playing = true
                        val handler = Handler(Looper.getMainLooper())
                        handler.post(
                            object : Runnable {
                                override fun run() {
                                    if (mediaPlayer?.isPlaying == true) { // 실제 재생 여부 확인
                                        onProgressUpdate(mp.currentPosition)
                                        handler.postDelayed(this, 50)
                                    }
                                }
                            },
                        )
                    }
                    setOnCompletionListener {
                        playing = false
                        stopPlaying()
                        onComplete()
                    }
                    setOnErrorListener { _, what, extra ->
                        Timber.e("MediaPlayer error: what=$what, extra=$extra")
                        false
                    }
                    prepareAsync()
                }
        }
    }

    fun stopPlaying() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
            }
        }
        mediaPlayer = null
        playing = false
        currentMessageUrl = null
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                pausePosition = it.currentPosition
                playing = false
            }
        }
    }

    fun getTotalDuration(): Int = mediaPlayer?.duration ?: 0

    fun isPlayingMessage(messageUrl: String) = playing && currentMessageUrl == messageUrl
}
