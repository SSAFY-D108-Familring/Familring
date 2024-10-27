package com.familring.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.presentation.screen.NoTimeCapsuleScreen
import com.familring.presentation.theme.FamilringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FamilringTheme {
                NoTimeCapsuleScreen()
            }
        }
    }
}
