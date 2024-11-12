package com.familring.presentation.screen.interest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.familring.presentation.theme.Black
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

    LaunchedEffect(Unit) {
        interestViewModel.uiEvent.collect { event ->
            when (event) {
                is InterestUiEvent.CreateSuccess -> {
                    onShowSnackBar("관심사를 등록했어요")
                }

                is InterestUiEvent.EditSuccess -> {
                    onShowSnackBar("관심사를 수정했어요")
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
            navigateToInterestList = navigateToInterestList,
            navigateToOtherInterest = navigateToOtherInterest,
            onNavigateBack = onNavigateBack,
        )
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
    shareImage: (MultipartBody.Part?) -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
    navigateToInterestList: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(state.isFamilyWrote && state.wroteFamilyCount >= 2) }

    LaunchedEffect(state.wroteFamilyCount) {
        if (state.wroteFamilyCount >= 2) {
            showDialog = true
        }
    }

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

                InterestState.NO_PERIOD -> {
                    ResultScreen(
                        selectedInterest = state.selectedInterest,
                        setPeriod = setPeriod,
                    )
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

        if (showDialog) {
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                InterestSelectDialog(
                    count = state.wroteFamilyCount,
                    selectInterest = {
                        selectInterest()
                        showDialog = false
                    },
                    closeDialog = { showDialog = false },
                )
            }
        }
    }
}

@Preview
@Composable
fun InterestScreenPreview() {
    InterestScreen()
}
