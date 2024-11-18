package com.familring.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import com.familring.presentation.theme.FamilringTheme
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.tag("FCM_TEST").d("Notification permission granted")
            } else {
                showNotificationPermissionDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotificationPermission()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        // 앱 처음 시작될 때 들어온 인텐트 처리
        handleDeepLink(intent)

        // apk 막기 용도
        val date = LocalDateTime.of(2024, 11, 24, 0, 0)

        setContent {
            FamilringTheme {
                if (LocalDateTime.now() > date) {
                    BlockScreen()
                } else {
                    MainScreen(
                        modifier =
                            Modifier
                                .statusBarsPadding()
                                .navigationBarsPadding(),
                    )
                }
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
            if (uri.scheme == "familring") {
                when (uri.host) {
                    "notification" -> {
                        val type = uri.getQueryParameter("type")
                        startDestination = when (type) {
                            "KNOCK", "RANDOM" -> "question"
                            "MENTION_SCHEDULE" -> "schedule"
                            "TIMECAPSULE" -> "timecapsule"
                            "INTEREST_PICK", "INTEREST_COMPLETE" -> "interest"
                            else -> null
                        }
                        Timber.d("딥링크로 이동할 화면: $startDestination")
                    }
                }
            } else {
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

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 권한이 이미 허용됨
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // 사용자가 이전에 거부했지만 "다시 묻지 않음"은 선택하지 않은 경우
                    showNotificationPermissionDialog()
                }

                else -> {
                    // 권한 요청
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("알림 권한 필요")
            .setMessage("가족들의 답변 요청을 받으려면 알림 권한이 필요합니다. 설정에서 알림을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                // 앱 설정 화면으로 이동
                openNotificationSettings()
            }.setNegativeButton("취소", null)
            .show()
    }

    private fun openNotificationSettings() {
        val intent =
            Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        startActivity(intent)
    }

    companion object {
        var startDestination: String? = null
    }
}
