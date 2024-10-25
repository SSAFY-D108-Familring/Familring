package com.familring.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.presentation.screen.TimeCapsuleScreen
import com.familring.presentation.theme.FamilringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FamilringTheme {
                TimeCapsuleScreen()
            }
        }
    }
}
