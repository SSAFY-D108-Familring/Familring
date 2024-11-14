package com.familring.presentation.screen.signup

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.toFile
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@Composable
fun PictureRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
    navigateToCount: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    PictureScreen(
        modifier = modifier,
        uiEvent = viewModel.event,
        popUpBackStack = popUpBackStack,
        showSnackBar = showSnackBar,
        navigateToCount = navigateToCount,
        updatePicture = viewModel::updateFace,
        getFaceCount = viewModel::getFaceCount,
    )

    if (uiState.isLoading) {
        LoadingDialog(
            loadingMessage = "얼굴을 인식하고 있어요...",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    modifier: Modifier = Modifier,
    uiEvent: SharedFlow<SignUpUiEvent>,
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    navigateToCount: () -> Unit = {},
    updatePicture: (File) -> Unit = {},
    getFaceCount: (File) -> Unit = {},
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    var faceSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showBottomSheet = true
    }

    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.FaceSuccess -> {
                    showSnackBar("사용 가능한 사진이에요! :)")
                    val file = imgUri?.toFile(context)
                    file?.let {
                        updatePicture(it)
                        faceSuccess = true
                    }
                }

                is SignUpUiEvent.FaceFail -> {
                    showSnackBar("분석하기에 올바르지 않은 사진이에요. 다시 한 번 촬영해 주세요!")
                    faceSuccess = false
                }

                is SignUpUiEvent.Error -> {
                    showSnackBar(event.message)
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(imgUri) {
        val file = imgUri?.toFile(context)
        if (file != null) {
            getFaceCount(file)
        }
    }

    val cameraFile =
        File.createTempFile("photo_", ".jpg", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
    val cameraFileUri =
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cameraFile,
        )
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            imgUri = cameraFileUri
        }

    // 권한 요청을 위한 launcher
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(cameraFileUri)
            }
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
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f)
                        .background(color = Gray04, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA,
                                ),
                                -> cameraLauncher.launch(cameraFileUri)

                                else -> permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
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
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "ic_camera",
                            tint = Gray02,
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "여기를 클릭해 사진을 촬영해 주세요",
                            style = Typography.bodyMedium,
                            color = Gray02,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            RoundLongButton(
                text = "촬영 완료",
                onClick = {
                    if (imgUri != null) {
                        navigateToCount()
                    } else {
                        showSnackBar("사진을 다시 한 번 촬영해 주세요!")
                    }
                },
                enabled = faceSuccess,
            )
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth()) {

            }
        }
    }
}
