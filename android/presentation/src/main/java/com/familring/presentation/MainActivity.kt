package com.familring.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.presentation.theme.FamilringTheme
import com.kakao.sdk.common.util.Utility

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FamilringTheme {
                MainScreen()

            }

            var keyHash = Utility.getKeyHash(this)
            Log.d("kjwTest", "keyHash: $keyHash")
        }
    }
}
