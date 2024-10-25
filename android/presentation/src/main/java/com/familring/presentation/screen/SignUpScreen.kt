package com.familring.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignUpScreen() {
}

@Composable
fun SignUpContent(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpContentPreview() {
    SignUpContent(Modifier)
}
