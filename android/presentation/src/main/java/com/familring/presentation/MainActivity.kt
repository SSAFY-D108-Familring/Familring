package com.familring.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.domain.Profile
import com.familring.presentation.screen.timecapsule.WritingTimeCapsuleScreen
import com.familring.presentation.theme.FamilringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FamilringTheme {
                WritingTimeCapsuleScreen(
                    letterCount = 2,
                    wroteProfiles =
                        listOf(
                            Profile("url1", "#FEE222"),
                            Profile("url1", "#FFE1E1"),
                            Profile("url1", "#FEE222"),
                        ),
                )
            }
        }
    }
}
