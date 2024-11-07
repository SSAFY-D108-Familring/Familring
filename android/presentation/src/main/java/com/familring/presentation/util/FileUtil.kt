package com.familring.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

fun ImageBitmap.toFile(context: Context): File? =
    try {
        val bitmap = this.asAndroidBitmap()
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "FACE_$timestamp.png"
        val file = File(context.cacheDir, filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun Uri.toFile(context: Context): File? =
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(this)
        val timestamp = "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}_${UUID.randomUUID()}"
        val filename = "DAILY_$timestamp.png"
        val file = File(context.cacheDir, filename)
        val outputStream: OutputStream = file.outputStream()

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        if (file.exists()) file else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
