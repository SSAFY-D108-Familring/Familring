package com.familring.presentation.screen.interest

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

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
) {
    val uiState by interestViewModel.uiState.collectAsStateWithLifecycle()

    InterestScreen(
        modifier = modifier,
        state = uiState,
        writeInterest = interestViewModel::createAnswer,
        editInterest = interestViewModel::updateAnswer,
        navigateToInterestList = navigateToInterestList,
        navigateToOtherInterest = navigateToOtherInterest,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun InterestScreen(
    modifier: Modifier = Modifier,
    navigateToInterestList: () -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    state: InterestUiState = InterestUiState(),
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
    shareImage: (Uri) -> Unit = {},
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
                        interest = state.interest,
                        isFamilyWrote = state.isFamilyWrote,
                        writeInterest = writeInterest,
                        editInterest = editInterest,
                        navigateToOtherInterest = navigateToOtherInterest,
                    )
                }

                InterestState.NO_PERIOD -> {
                    ResultScreen(
                        result = state.interest,
                        nickname = "나갱",
                        navigateToPeriod = navigateToOtherInterest,
                    )
                }

                InterestState.MISSION -> {
                    ShareDayScreen(
                        isUpload = state.isUploadMission,
                        shareImage = shareImage,
                        navigateToOtherInterest = navigateToOtherInterest,
                    )
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
