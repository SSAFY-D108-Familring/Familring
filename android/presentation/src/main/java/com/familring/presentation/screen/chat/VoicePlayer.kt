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
        onError: (String) -> Unit,
    ) {
        try {
            if (mediaPlayer != null && isInitialized && currentMessageUrl == filePath) {
                mediaPlayer?.let {
                    it.seekTo(pausePosition)
                    it.start()
                    startProgressUpdate(it, onProgressUpdate)
                }
                playing = true
                return
            }
            resetMediaPlayer()
            mediaPlayer =
                MediaPlayer().apply {
                    setDataSource(filePath)
                    setOnErrorListener { _, what, extra ->
                        Timber.e("MediaPlayer error: what=$what, extra=$extra")
                        resetMediaPlayer()
                        onComplete() // 에러 시 ui 완료 상태로
                        onError("오류가 발생했어요. :( 음성 메시지를 다시 재생해 주세요.")
                        true
                    }
                    setOnPreparedListener { mp ->
                        isInitialized = true
                        mp.start()
                        playing = true
                        startProgressUpdate(mp, onProgressUpdate)
                    }
                    setOnCompletionListener {
                        playing = false
                        stopPlaying()
                        onComplete()
                    }
                    prepareAsync()
                }
            currentMessageUrl = filePath
        } catch (e: Exception) {
            Timber.e("Error playing message: ${e.message}")
            resetMediaPlayer()
            onComplete() // 오류 시 재생 중단
        }
    }

    // Progress 업데이트 함수 분리
    private fun startProgressUpdate(
        mp: MediaPlayer,
        onProgressUpdate: (Int) -> Unit,
    ) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(
            object : Runnable {
                override fun run() {
                    if (mp.isPlaying) {
                        onProgressUpdate(mp.currentPosition)
                        handler.postDelayed(this, 50)
                    }
                }
            },
        )
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

    private fun resetMediaPlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        playing = false
        pausePosition = 0
        currentMessageUrl = null
        isInitialized = false
    }

    fun getTotalDuration(): Int = mediaPlayer?.duration ?: 0

    fun isPlayingMessage(messageUrl: String) = playing && currentMessageUrl == messageUrl
}
