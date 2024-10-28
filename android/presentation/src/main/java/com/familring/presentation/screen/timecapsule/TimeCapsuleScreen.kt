@file:JvmName("WritingTimeCapsuleScreenKt")

package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.familring.presentation.component.CustomTab

@Composable
fun TimeCapsuleScreen(modifier: Modifier = Modifier) {
    val tabs = listOf("작성", "목록")
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CustomTab(
                selectedItemIndex = selectedItemIndex,
                tabs = tabs,
                onClick = { selectedItemIndex = it },
            )
            when (selectedItemIndex) {
                0 -> WritingTimeCapsuleScreen()
                1 ->
                    TimeCapsuleListScreen(
                        navigationToCapsule = {},
                    )
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleScreenPreview() {
    TimeCapsuleScreen()
}
