package com.familring.presentation.screen.timecapsule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.familring.domain.model.timecapsule.TimeCapsule
import com.familring.presentation.R
import com.familring.presentation.component.CustomTextTab
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TutorialScreen
import com.familring.presentation.component.dialog.TwoButtonTextDialog
import com.familring.presentation.screen.timecapsule.WritingState.WRITING_TIME_CAPSULE
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToCreate: () -> Unit,
    timeCapsuleViewModel: TimeCapsuleViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit = {},
) {
    val uiState by timeCapsuleViewModel.uiState.collectAsStateWithLifecycle()
    val timeCapsules = timeCapsuleViewModel.getTimeCapsulePagination().collectAsLazyPagingItems()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTutorial by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        timeCapsuleViewModel.event.collect { event ->
            when (event) {
                is TimeCapsuleUiEvent.Success -> {
                    timeCapsuleViewModel.getTimeCapsuleStatus()
                }

                is TimeCapsuleUiEvent.Error -> {
                    onShowSnackBar(event.message)
                }
            }
        }
    }

    TimeCapsuleScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToCreate = navigateToCreate,
        state = uiState,
        timeCapsules = timeCapsules,
        showTutorial = {
            showTutorial = true
            timeCapsuleViewModel.setReadTutorialState(false)
        },
        getTimeCapsuleStatus = timeCapsuleViewModel::getTimeCapsuleStatus,
        createTimeCapsuleAnswer = timeCapsuleViewModel::createTimeCapsuleAnswer,
    )

    if (showTutorial && !uiState.isReadTutorial) {
        ModalBottomSheet(
            containerColor = White,
            onDismissRequest = {
                showTutorial = false
                timeCapsuleViewModel.setReadTutorial()
            },
            sheetState = sheetState,
        ) {
            TutorialScreen(
                imageLists =
                    listOf(
                        R.drawable.img_tutorial_timecapsule_first,
                        R.drawable.img_tutorial_timecapsule_second,
                        R.drawable.img_tutorial_timecapsule_third,
                        R.drawable.img_tutorial_timecapsule_fourth,
                    ),
                title = "타임캡슐 미리보기 \uD83D\uDD0D",
                subTitle =
                    "일정 기간을 정한 뒤  가족의 타임캡슐을 \n" +
                        "작성하고 나중에 열어볼 수 있어요!",
            )
        }
    }
}

@Composable
fun TimeCapsuleScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToCreate: () -> Unit = {},
    state: TimeCapsuleUiState,
    timeCapsules: LazyPagingItems<TimeCapsule>,
    showTutorial: () -> Unit = {},
    getTimeCapsuleStatus: () -> Unit = {},
    createTimeCapsuleAnswer: (String) -> Unit = {},
) {
    val tabs = listOf("작성", "목록")
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

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
                        text = "타임 캡슐",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
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
                        painter = painterResource(id = R.drawable.ic_question),
                        contentDescription = "ic_question",
                        tint = Gray03,
                    )
                },
                onNavigationClick = {
                    if (state.writingStatus == WRITING_TIME_CAPSULE) {
                        showDialog = true
                    } else {
                        popUpBackStack()
                    }
                },
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CustomTextTab(
                selectedItemIndex = selectedItemIndex,
                tabs = tabs,
                onClick = { selectedItemIndex = it },
                spaceSize = 40,
            )
            when (selectedItemIndex) {
                0 -> {
                    WritingTimeCapsuleScreen(
                        state = state,
                        navigateToCreate = navigateToCreate,
                        getTimeCapsuleStatus = getTimeCapsuleStatus,
                        createTimeCapsuleAnswer = createTimeCapsuleAnswer,
                    )
                }

                1 -> {
                    TimeCapsuleListScreen(
                        timeCapsules = timeCapsules,
                    )
                }
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                TwoButtonTextDialog(
                    text = "타임 캡슐 작성을 종료하시겠어요?",
                    onConfirmClick = {
                        showDialog = false
                        popUpBackStack()
                    },
                    onDismissClick = { showDialog = false },
                )
            }
        }

        BackHandler(enabled = true) {
            if (state.writingStatus == WRITING_TIME_CAPSULE && selectedItemIndex == 0) {
                showDialog = true
            } else {
                popUpBackStack()
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleScreenPreview() {
//    TimeCapsuleScreen(
//        state =
//            TimeCapsuleUiState(
//                writingStatus = 2,
//            ),
//    )
}
