package com.familring.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.familring.presentation.screen.login.LoginScreen
import com.familring.domain.Profile
import com.familring.presentation.screen.question.QuestionListPreview
import com.familring.presentation.screen.question.QuestionListScreen
import com.familring.presentation.screen.timecapsule.WritingTimeCapsuleScreen
import com.familring.presentation.screen.question.QuestionScreen
import com.familring.presentation.theme.FamilringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FamilringTheme {
                Scaffold { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        QuestionListScreen()
                    }
                }
            }
        }
    }
}
