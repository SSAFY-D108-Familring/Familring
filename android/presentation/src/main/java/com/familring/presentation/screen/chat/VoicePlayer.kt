package com.familring.presentation.screen.chat

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import timber.log.Timber

class VoicePlayer {
    private var mediaPlayer: MediaPlayer? = null
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
            if (mediaPlayer != null && currentMessageUrl == filePath) {
                mediaPlayer?.let {
                    it.seekTo(pausePosition)
                    it.start()
                    startProgressUpdate(it, onProgressUpdate)
                }
                playing = true
                return
            }
            mediaPlayer =
                MediaPlayer().apply {
                    setDataSource(filePath)
                    prepareAsync()
                    setOnErrorListener { _, what, extra ->
                        Timber.e("MediaPlayer error: what=$what, extra=$extra")
                        true
                    }
                    setOnPreparedListener { mp ->
                        mp.start()
                        playing = true
                        startProgressUpdate(mp, onProgressUpdate)
                    }
                    setOnCompletionListener {
                        playing = false
                        stopPlaying()
                        onComplete()
                    }
                }
            currentMessageUrl = filePath
        } catch (e: Exception) {
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
