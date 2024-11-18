package com.familring.presentation.screen.interest

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TutorialScreen
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import okhttp3.MultipartBody
import java.time.LocalDate

object InterestState {
    const val WRITING = 0
    const val NO_PERIOD = 1
    const val MISSION = 2
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestRoute(
    modifier: Modifier,
    navigateToInterestList: () -> Unit,
    navigateToOtherInterest: () -> Unit,
    onNavigateBack: () -> Unit,
    interestViewModel: InterestViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val uiState by interestViewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTutorial by remember { mutableStateOf(true) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        interestViewModel.uiEvent.collect { event ->
            when (event) {
                is InterestUiEvent.CreateSuccess -> {
                    onShowSnackBar("관심사를 등록했어요")
                }

                is InterestUiEvent.EditSuccess -> {
                    onShowSnackBar("관심사를 수정했어요")
                }

                is InterestUiEvent.ShowDialog -> {
                    if (uiState.isFamilyWrote) {
                        showDialog = true
                    }
                }

                is InterestUiEvent.Error -> {
                    onShowSnackBar(event.message)
                }
            }
        }
    }

    if (!uiState.isInterestScreenLoading) {
        InterestScreen(
            modifier = modifier,
            state = uiState,
            writeInterest = interestViewModel::createAnswer,
            editInterest = interestViewModel::updateAnswer,
            selectInterest = interestViewModel::selectInterest,
            setPeriod = interestViewModel::setMissionPeriod,
            shareImage = interestViewModel::postMission,
            showTutorial = {
                showTutorial = true
                interestViewModel.setReadTutorialState(false)
            },
            navigateToInterestList = navigateToInterestList,
            navigateToOtherInterest = navigateToOtherInterest,
            onNavigateBack = onNavigateBack,
        )

        if (showTutorial && !uiState.isReadTutorial) {
            ModalBottomSheet(
                containerColor = White,
                onDismissRequest = {
                    showTutorial = false
                    interestViewModel.setReadTutorial()
                },
                sheetState = sheetState,
            ) {
                TutorialScreen(
                    imageLists =
                        listOf(
                            R.drawable.img_tutorial_interest_first,
                            R.drawable.img_tutorial_interest_second,
                            R.drawable.img_tutorial_interest_third,
                            R.drawable.img_tutorial_interest_fourth,
                        ),
                    title = "관심사 공유 미리보기 \uD83D\uDD0D",
                    subTitle =
                        "가족들의 최근 관심사를 알아보고\n" +
                            "체험한 후 인증하며 가까워질 수 있어요!",
                )
            }
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            InterestSelectDialog(
                count = uiState.wroteFamilyCount,
                selectInterest = {
                    interestViewModel.selectInterest()
                    showDialog = false
                },
                closeDialog = { showDialog = false },
            )
        }
    }
}

@Composable
fun InterestScreen(
    modifier: Modifier = Modifier,
    state: InterestUiState = InterestUiState(),
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
    selectInterest: () -> Unit = {},
    setPeriod: (LocalDate) -> Unit = {},
    showTutorial: () -> Unit = {},
    shareImage: (MultipartBody.Part?) -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
    navigateToInterestList: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
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
                        text = stringResource(R.string.interest_top_title),
                        color = Black,
                        style = Typography.titleLarge.copy(fontSize = 30.sp),
                    )
                },
                onNavigationClick = onNavigateBack,
                tutorialIcon = {
                    Icon(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .border(
                                    width = 2.dp,
                                    color = Gray03,
                                    shape = CircleShape,
                                ).padding(2.dp)
                                .noRippleClickable { showTutorial() },
                        painter = painterResource(id = R.drawable.ic_tutorial),
                        contentDescription = "ic_question",
                        tint = Gray03,
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier =
                            Modifier.noRippleClickable {
                                navigateToInterestList()
                            },
                        painter = painterResource(id = R.drawable.img_menu),
                        contentDescription = "img_menu",
                    )
                },
            )
            Spacer(modifier = Modifier.height(12.dp))

            when (state.interestStatus) {
                InterestState.WRITING -> {
                    if (!state.isWritingScreenLoading) {
                        WriteDayScreen(
                            modifier = Modifier.imePadding(),
                            isWroteInterest = state.isWroteInterest,
                            interest = state.myInterest,
                            isFamilyWrote = state.isFamilyWrote,
                            writeInterest = writeInterest,
                            editInterest = editInterest,
                            navigateToOtherInterest = navigateToOtherInterest,
                        )
                    }
                }

                InterestState.NO_PERIOD -> {
                    if (!state.isResultScreenLoading) {
                        ResultScreen(
                            selectedInterest = state.selectedInterest,
                            setPeriod = setPeriod,
                        )
                    }
                }

                InterestState.MISSION -> {
                    if (!state.isShareScreenLoading) {
                        ShareDayScreen(
                            isUpload = state.isUploadMission,
                            selectedInterest = state.selectedInterest,
                            leftMissionPeriod = state.leftMissionPeriod,
                            missions = state.missions,
                            shareImage = shareImage,
                            navigateToOtherInterest = navigateToOtherInterest,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InterestScreenPreview() {
    InterestScreen()
}
