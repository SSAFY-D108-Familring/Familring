package com.familring.presentation.component.dialog

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography

/**
 * TODO
 *
 * @param modifier
 * @param lottieRes : 로티 파일
 * @param loadingMessage : 로딩 메시지
 */
@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.loading,
    loadingMessage: String = "로딩중이에요...",
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieRes))
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Black.copy(alpha = 0.6f),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = lottieComposition,
                restartOnPlay = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxHeight(0.25f),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = loadingMessage,
                style = Typography.displayLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    }
}
