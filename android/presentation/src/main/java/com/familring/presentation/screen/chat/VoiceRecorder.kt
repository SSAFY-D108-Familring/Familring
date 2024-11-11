package com.familring.presentation.screen.chat

import android.content.Context
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VoiceRecorder(
    private val context: Context,
) {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var recording = false
    private var amplitudes = mutableListOf<Int>()
    private var amplitudeJob: Job? = null

    fun startRecording(): Boolean {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileNameWithTimestamp = "AUDIO_MESSAGE_$timeStamp"

        // 이미 녹음 중이면 종료
        if (recording) {
            stopRecording()
        }

        // 파일 저장 위치 지정
        outputFile = File(context.cacheDir, "$fileNameWithTimestamp.m4a")

        mediaRecorder =
            MediaRecorder().apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(outputFile?.absolutePath)
                    prepare()
                    start()
                    recording = true
                    startAmplitudeMonitoring()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }
            }
        return true
    }

    fun stopRecording(): File? {
        amplitudeJob?.cancel()
        mediaRecorder?.apply {
            if (recording) {
                stop()
            }
            release()
        }
        recording = false
        mediaRecorder = null
        return outputFile
    }

    private fun startAmplitudeMonitoring() {
        amplitudes.clear()
        amplitudeJob =
            CoroutineScope(Dispatchers.IO).launch {
                while (isActive && recording && mediaRecorder != null) {
                    try {
                        val amplitude = mediaRecorder?.maxAmplitude
                        if (amplitude != null) {
                            amplitudes.add(amplitude)
                        } else {
                            break
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        break
                    }
                    delay(100)
                }
            }
    }

    fun getAmplitudes(): List<Int> = amplitudes.toList()
}
