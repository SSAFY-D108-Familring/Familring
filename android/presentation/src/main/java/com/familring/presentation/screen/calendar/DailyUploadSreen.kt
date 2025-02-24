package com.familring.presentation.screen.calendar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.util.toMultiPart
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.component.textfield.GrayBackgroundTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.createCameraFile
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toFile
import okhttp3.MultipartBody
import java.io.File

@Composable
fun DailyUploadRoute(
    modifier: Modifier = Modifier,
    targetDaily: DailyLife,
    isModify: Boolean,
    dailyViewModel: DailyViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    showSnackbar: (String) -> Unit,
) {
    val uiState by dailyViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyViewModel.event.collect { event ->
            when (event) {
                is DailyUiEvent.Success -> {
                    popUpBackStack()
                }

                is DailyUiEvent.Error -> {
                    showSnackbar(event.message)
                }
            }
        }
    }

    DailyUploadScreen(
        modifier = modifier,
        targetDaily = targetDaily,
        isModify = isModify,
        createDaily = dailyViewModel::createDaily,
        modifyDaily = dailyViewModel::updateDaily,
        popUpBackStack = popUpBackStack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyUploadScreen(
    modifier: Modifier = Modifier,
    targetDaily: DailyLife,
    isModify: Boolean = false,
    createDaily: (String, MultipartBody.Part?) -> Unit = { _, _ -> },
    modifyDaily: (Long, String, MultipartBody.Part?) -> Unit = { _, _, _ -> },
    popUpBackStack: () -> Unit = {},
) {
    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }

    var imgUri by remember {
        mutableStateOf(if (isModify) targetDaily.dailyImgUrl.toUri() else null)
    }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    imgUri = it
                }
            },
        )

    var tempUri = Uri.EMPTY
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                imgUri = tempUri
            }
        }

    // 권한 요청을 위한 launcher
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                val (cameraFile, cameraFileUri) = createCameraFile(context)
                tempUri = cameraFileUri
                cameraLauncher.launch(cameraFileUri)
            }
        }

    val textFieldState =
        remember { TextFieldState(if (isModify) targetDaily.content else "") }
    val contentScrollState = rememberScrollState()

    val scrollState = rememberScrollState()

    // 키보드 높이 감지
    val imeInsets = WindowInsets.ime.exclude(WindowInsets.navigationBars)
    val imeHeight = imeInsets.getBottom(LocalDensity.current)

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(imeHeight) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = with(LocalDensity.current) { (imeHeight).toDp() }) // 키보드 높이만큼 padding을 추가
                    .verticalScroll(state = scrollState),
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
                onNavigationClick = { showDialog = true },
            )
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(3.5f / 3f)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = Gray04, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            showBottomSheet = true
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (imgUri != null) {
                    AsyncImage(
                        modifier =
                            Modifier
                                .matchParentSize()
                                .clip(shape = RoundedCornerShape(12.dp)),
                        model = imgUri,
                        contentScale = ContentScale.Crop,
                        contentDescription = "daily",
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier = Modifier.size(60.dp),
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "ic_camera",
                            tint = Gray01,
                        )
                        Text(
                            modifier = Modifier.padding(top = 3.dp),
                            text = "사진 등록은 필수입니다",
                            style =
                                Typography.displaySmall.copy(
                                    fontSize = 18.sp,
                                    color = Gray02,
                                ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            GrayBackgroundTextField(
                textFieldState = textFieldState,
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(2.8f / 2f),
                scrollState = contentScrollState,
                hint = "가족과 공유하고 싶은 일상을 작성해 주세요!",
            )
            RoundLongButton(
                modifier =
                    Modifier
                        .padding(vertical = 10.dp),
                text = if (!isModify) "일상 등록하기" else "일상 수정하기",
                onClick = {
                    if (!isModify) {
                        createDaily(
                            textFieldState.text.toString(),
                            imgUri?.toFile(context).toMultiPart(filename = "image"),
                        )
                    } else {
                        modifyDaily(
                            targetDaily.dailyId,
                            textFieldState.text.toString(),
                            imgUri?.toFile(context).toMultiPart(filename = "image"),
                        )
                    }
                },
                enabled = imgUri != null,
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = White,
                onDismissRequest = {
                    showBottomSheet = false
                },
            ) {
                Column(
                    modifier =
                        Modifier
                            .padding(vertical = 20.dp),
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        when (PackageManager.PERMISSION_GRANTED) {
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA,
                                            ),
                                            -> {
                                                val (cameraFile, cameraFileUri) =
                                                    createCameraFile(
                                                        context,
                                                    )
                                                tempUri = cameraFileUri
                                                cameraLauncher.launch(cameraFileUri)
                                            }

                                            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                        showBottomSheet = false
                                    },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = R.drawable.img_camera),
                                contentDescription = "img_camera",
                            )
                            Text(
                                text = "촬영",
                                color = Black,
                                style = Typography.headlineMedium.copy(fontSize = 22.sp),
                            )
                        }
                        Column(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        singlePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                        )
                                        showBottomSheet = false
                                    },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.img_album),
                                contentDescription = "img_album",
                            )
                            Text(
                                text = "갤러리",
                                color = Black,
                                style = Typography.headlineMedium.copy(fontSize = 22.sp),
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = if (!isModify) "일상 업로드를 종료하시겠어요?" else "일상 수정을 종료하시겠어요?",
                    onConfirmClick = {
                        showDialog = false
                        popUpBackStack()
                    },
                    onDismissClick = { showDialog = false },
                )
            }
        }

        BackHandler(enabled = !showDialog) {
            showDialog = true
        }
    }
}

@Preview
@Composable
private fun DailyUploadScreenPreview() {
    DailyUploadScreen(
        targetDaily = DailyLife(),
        isModify = true,
    )
}
