package com.familring.presentation.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.domain.model.Profile
import com.familring.domain.request.UserEmotionRequest
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.screen.home.EmotionUpdateScreen
import com.familring.presentation.screen.signup.shareCode
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPageRoute(
    modifier: Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToEditName: () -> Unit,
    navigateToEditColor: () -> Unit,
    navigateToEditFace: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MyPageUiEvent.SignOutSuccess -> {
                    showSnackBar("패밀링을 이용해 주셔서 감사합니다 :)")
                    navigateToLogin()
                }

                is MyPageUiEvent.EmotionUpdateSuccess -> {
                    showSnackBar("나의 현재 기분이 변경되었어요!")
                }

                is MyPageUiEvent.Error -> showSnackBar(event.message)

                else -> {}
            }
        }
    }

    HandleMyPageUi(
        modifier = modifier,
        uiState = uiState,
        signOut = viewModel::signOut,
        logOut = viewModel::logOut,
        popUpBackStack = popUpBackStack,
        showSnackBar = showSnackBar,
        updateEmotion = viewModel::updateEmotion,
        navigateToEditName = navigateToEditName,
        navigateToEditColor = navigateToEditColor,
        navigateToEditFace = navigateToEditFace,
    )
}

@Composable
fun HandleMyPageUi(
    modifier: Modifier = Modifier,
    uiState: MyPageUiState,
    signOut: () -> Unit = {},
    logOut: () -> Unit = {},
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    updateEmotion: (UserEmotionRequest) -> Unit = {},
    navigateToEditName: () -> Unit = {},
    navigateToEditColor: () -> Unit = {},
    navigateToEditFace: () -> Unit = {},
) {
    when (uiState) {
        MyPageUiState.Loading -> LoadingDialog()
        is MyPageUiState.Success ->
            MyPageScreen(
                modifier = modifier,
                popUpBackStack = popUpBackStack,
                navigateToEditName = navigateToEditName,
                navigateToEditColor = navigateToEditColor,
                navigateToEditFace = navigateToEditFace,
                signOut = signOut,
                logOut = logOut,
                showSnackBar = showSnackBar,
                nickname = uiState.userNickname,
                birthDate = uiState.userBirthDate,
                role = uiState.userRole,
                profileImage = uiState.profileImage,
                userColor = uiState.userColor,
                emotion = uiState.userEmotion,
                code = uiState.code,
                updateEmotion = updateEmotion,
            )
    }
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToEditName: () -> Unit = {},
    navigateToEditColor: () -> Unit = {},
    navigateToEditFace: () -> Unit = {},
    signOut: () -> Unit = {},
    logOut: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    updateEmotion: (UserEmotionRequest) -> Unit = {},
    nickname: String = "",
    birthDate: String = "",
    role: String = "",
    emotion: String = "",
    profileImage: String = "0xFFFFFFFF",
    userColor: String = "",
    code: String = "",
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showCodeDialog by remember { mutableStateOf(false) }
    var showEmotionDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "마이페이지",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ZodiacBackgroundProfile(
                        profile =
                            Profile(
                                nickname = nickname,
                                zodiacImgUrl = profileImage,
                                backgroundColor = userColor,
                            ),
                        size = 135,
                        paddingValue = 20,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = nickname,
                            style = Typography.headlineSmall.copy(fontSize = 20.sp),
                            color = Black,
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = roleToWord(role),
                            style = Typography.bodyLarge,
                            color = Gray01,
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = birthDate,
                        style = Typography.bodyMedium,
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = emotion.ifEmpty { "평범해요 🙂" },
                        style = Typography.displayMedium.copy(fontSize = 16.sp),
                        color = Gray01,
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable { showEmotionDialog = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "내 기분 설정하기",
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "emotion",
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable { showCodeDialog = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "가족코드 확인하기",
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "family_code",
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .noRippleClickable { navigateToEditName() }
                        .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "닉네임 변경하기",
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "nickname",
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable { navigateToEditColor() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "배경색 변경하기",
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "profile_color",
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable { navigateToEditFace() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "얼굴 사진 수정하기",
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "face",
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.5f))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        modifier = Modifier.noRippleClickable { showLogoutDialog = true },
                        text = "로그아웃",
                        style = Typography.bodyMedium,
                        color = Gray01,
                    )
                    VerticalDivider(
                        modifier = Modifier.height(15.dp),
                        thickness = 1.dp,
                        color = Gray01,
                    )
                    Text(
                        modifier = Modifier.noRippleClickable { showSignOutDialog = true },
                        text = "회원탈퇴",
                        style = Typography.bodyMedium,
                        color = Red01,
                    )
                }
            }
        }
    }

    if (showSignOutDialog) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.5f))
                    .noRippleClickable { showSignOutDialog = false },
            contentAlignment = Alignment.Center,
        ) {
            TwoButtonTextDialog(
                text = "정말 패밀링을 탈퇴하시겠어요?",
                onConfirmClick = {
                    showSignOutDialog = false
                    signOut()
                },
                onDismissClick = {
                    showSignOutDialog = false
                },
            )
        }
    }

    if (showLogoutDialog) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.5f))
                    .noRippleClickable { showLogoutDialog = false },
            contentAlignment = Alignment.Center,
        ) {
            TwoButtonTextDialog(
                text = "로그아웃하시겠어요?",
                onConfirmClick = {
                    showLogoutDialog = false
                    logOut()
                },
                onDismissClick = {
                    showLogoutDialog = false
                },
            )
        }
    }

    if (showCodeDialog) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.5f))
                    .noRippleClickable { showCodeDialog = false },
            contentAlignment = Alignment.Center,
        ) {
            TwoButtonTextDialog(
                modifier = Modifier.clickable(enabled = false) {},
                text = code,
                titleStyle = Typography.titleMedium,
                confirmText = "카톡으로 공유",
                dismissText = "복사하기",
                onConfirmClick = {
                    shareCode(code, context, showSnackBar)
                    showCodeDialog = false
                },
                onDismissClick = {
                    clipboardManager.setText(AnnotatedString(code))
                    showSnackBar("클립보드에 복사되었습니다!")
                    showCodeDialog = false
                },
            )
        }
    }

    if (showEmotionDialog) {
        EmotionUpdateScreen(
            onClose = { showEmotionDialog = false },
            clickEmotion = {
                // 서버에 기분 수정 보내기
                updateEmotion(UserEmotionRequest(it))
                showEmotionDialog = false
            },
        )
    }
}

fun roleToWord(role: String): String =
    when (role) {
        "M" -> "엄마"
        "F" -> "아빠"
        "D" -> "딸"
        else -> "아들"
    }

@Preview
@Composable
fun MyPagePreview() {
    MyPageScreen(
        code = "4D53A4",
        nickname = "나갱이",
        userColor = "0xFFFFE1E1",
        role = "D",
        birthDate = "2002-01-28",
        emotion = "평범해요 🙂",
    )
}
