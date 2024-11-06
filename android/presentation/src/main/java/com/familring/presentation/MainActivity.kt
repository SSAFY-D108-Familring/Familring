package com.familring.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.familring.presentation.theme.FamilringTheme
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 앱 처음 시작될 때 들어온 인텐트 처리
        handleDeepLink(intent)

        setContent {
            FamilringTheme {
                MainScreen()
            }
        }
        Timber.tag("keyhash :").d(Utility.getKeyHash(this))
    }

    // 앱 이미 실행 중일 때 새로운 인텐트로 들어올 경우 처리
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        intent.data?.let { uri ->
            val action = uri.getQueryParameter("action")
            val code = uri.getQueryParameter("code")

            if (action == "copy_code" && code != null) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("family_code", code)
                clipboard.setPrimaryClip(clip)
            }
        }
    }
}
