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
    private var handler: Handler? = null

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
                        onError("MediaPlayer error occurred.")
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
            onError("Failed to play the message.")
        }
    }

    private fun startProgressUpdate(
        mp: MediaPlayer,
        onProgressUpdate: (Int) -> Unit,
    ) {
        handler = Handler(Looper.getMainLooper())
        handler?.post(
            object : Runnable {
                override fun run() {
                    if (mediaPlayer != null && mp.isPlaying) {
                        onProgressUpdate(mp.currentPosition)
                        handler?.postDelayed(this, 50)
                    }
                }
            },
        )
    }

    fun stopPlaying() {
        handler?.removeCallbacksAndMessages(null)
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
            }
        }
        resetMediaPlayer()
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
        handler?.removeCallbacksAndMessages(null)
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
