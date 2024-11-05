package com.familring.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.presentation.theme.FamilringTheme
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamilringTheme {
                MainScreen()
            }

            Log.d("dg","keyhash : ${Utility.getKeyHash(this)}")
        }
        Log.d("keyhash :", "${Utility.getKeyHash(this)}")
    }
}
