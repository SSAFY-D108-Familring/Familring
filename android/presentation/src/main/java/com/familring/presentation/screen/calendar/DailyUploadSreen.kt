package com.familring.presentation.screen.calendar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.familring.presentation.R
import com.familring.presentation.component.GrayBackgroundTextField
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun DailyUploadRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
) {
    DailyUploadScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyUploadScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
) {
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    imgUri = it
                }
            },
        )

    var content by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(content) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "내 일상 업로드",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(3f / 4f)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = Gray04, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (imgUri != null) {
                    GlideImage(
                        modifier =
                            Modifier
                                .matchParentSize()
                                .clip(shape = RoundedCornerShape(12.dp)),
                        model = imgUri,
                        contentScale = ContentScale.Crop,
                        contentDescription = "daily",
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
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            GrayBackgroundTextField(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f),
                content = content,
                scrollState = scrollState,
                onValueChange = { content = it },
                hint = "가족과 공유하고 싶은 일상을 작성해 주세요!",
            )
            RoundLongButton(
                modifier = Modifier.padding(vertical = 20.dp),
                text = "일상 등록하기",
                onClick = popUpBackStack,
                enabled = imgUri != null,
            )
        }
    }
}

@Preview
@Composable
private fun DailyUploadScreenPreview() {
    DailyUploadScreen()
}
