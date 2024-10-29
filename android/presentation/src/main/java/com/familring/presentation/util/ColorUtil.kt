package com.familring.presentation.util

import androidx.compose.ui.graphics.Color

/**
 * String 컬러 코드 값은 0xFF608B43 형식이어야 함
 * */
fun String.toColor(): Color {
    val colorInt = this.removePrefix("0x").toLong(radix = 16).toInt()
    return Color(colorInt)
}
