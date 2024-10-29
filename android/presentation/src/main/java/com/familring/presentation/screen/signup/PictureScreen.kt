package com.familring.presentation.screen.signup

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.familring.presentation.R
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography

@Composable
fun PictureRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToCount: () -> Unit,
) {
    PictureScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToCount = navigateToCount,
    )
}

@Composable
fun PictureScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToCount: () -> Unit = {},
) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bmp ->
                bmp?.let {
                    bitmap = it.asImageBitmap()
                }
            },
        )

    // 권한 요청을 위한 launcher
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(null)
            }
        }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "사진 촬영",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                text = "얼굴 분석을 위해 정면 사진을 촬영해 주세요",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                text = "최대한 이목구비가 잘 나오게요!",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(3f / 4f)
                        .background(color = Gray04, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA,
                                ),
                                -> cameraLauncher.launch(null)

                                else -> permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (bitmap != null) {
                    Image(
                        modifier = Modifier.matchParentSize(),
                        bitmap = bitmap!!,
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile",
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "ic_camera",
                        tint = Gray01,
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            RoundLongButton(
                text = "촬영 완료",
                onClick = navigateToCount,
                enabled = bitmap != null,
            )
        }
    }
}

@Preview
@Composable
fun PicturePreview() {
    PictureScreen()
}
