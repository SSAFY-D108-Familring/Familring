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
import com.familring.presentation.R
import com.familring.presentation.component.EmotionGrid
import com.familring.presentation.component.LoadingDialog
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TwoButtonTextDialog
import com.familring.presentation.component.ZodiacBackgroundProfile
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
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MyPageUiEvent.SignOutSuccess -> {
                    showSnackBar("회원 탈퇴 완료")
                    navigateToLogin()
                }

                is MyPageUiEvent.Error -> showSnackBar(event.message)
            }
        }
    }

    HandleMyPageUi(
        modifier = modifier,
        uiState = uiState,
        signOut = viewModel::signOut,
        popUpBackStack = popUpBackStack,
        showSnackBar = showSnackBar,
        updateEmotion = viewModel::updateEmotion,
        navigateToEditName = navigateToEditName,
    )
}

@Composable
fun HandleMyPageUi(
    modifier: Modifier = Modifier,
    uiState: MyPageUiState,
    signOut: () -> Unit = {},
    popUpBackStack: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    updateEmotion: (String) -> Unit = {},
    navigateToEditName: () -> Unit = {},
) {
    when (uiState) {
        MyPageUiState.Loading -> LoadingDialog()
        is MyPageUiState.Success ->
            MyPageScreen(
                modifier = modifier,
                popUpBackStack = popUpBackStack,
                navigateToEditName = navigateToEditName,
                signOut = signOut,
                showSnackBar = showSnackBar,
                nickname = uiState.userNickname,
                birthDate = uiState.userBirthDate,
                role = uiState.userRole,
                profileImage = uiState.profileImage,
                userColor = uiState.userColor,
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
    signOut: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    updateEmotion: (String) -> Unit = {},
    nickname: String = "",
    birthDate: String = "",
    role: String = "",
    profileImage: String = "",
    userColor: String = "",
    code: String = "",
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showCodeDialog by remember { mutableStateOf(false) }
    var showEmotionDialog by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
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
                                nickName = nickname,
                                zodiacImgUrl = profileImage,
                                backgroundColor = userColor,
                            ),
                        size = 135,
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
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
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
                    contentDescription = "family_code",
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
                    contentDescription = "family_code",
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Text(
                modifier =
                    Modifier
                        .padding(start = 15.dp)
                        .noRippleClickable { showSignOutDialog = true },
                text = "회원탈퇴",
                style = Typography.bodyMedium,
                color = Red01,
            )
        }
    }

    if (showSignOutDialog) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.5f)),
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
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.5f))
                    .noRippleClickable { showEmotionDialog = false },
            contentAlignment = Alignment.Center,
        ) {
            EmotionGrid(
                clickEmotion = { emotion ->
                    // 서버에 기분 수정 보내기
                    updateEmotion(emotion)
                    showEmotionDialog = false
                },
            )
        }
    }
}

private fun roleToWord(role: String): String =
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
    )
}
