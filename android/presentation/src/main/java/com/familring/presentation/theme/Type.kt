package com.familring.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.familring.presentation.R

val letterSpacing = (-0.06).em
val Typography =
    Typography(
        titleLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 30.sp,
                letterSpacing = letterSpacing,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 28.sp,
                letterSpacing = letterSpacing,
            ),
        titleSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 24.sp,
                letterSpacing = letterSpacing,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 24.sp,
                letterSpacing = letterSpacing,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 20.sp,
                letterSpacing = letterSpacing,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        displayLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 24.sp,
                letterSpacing = letterSpacing,
            ),
        displayMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 20.sp,
                letterSpacing = letterSpacing,
            ),
        displaySmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 18.sp,
                letterSpacing = letterSpacing,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        bodySmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = letterSpacing,
            ),
        labelLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                fontSize = 18.sp,
                letterSpacing = letterSpacing,
            ),
        labelMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        labelSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                fontSize = 14.sp,
                letterSpacing = letterSpacing,
            ),
    )
