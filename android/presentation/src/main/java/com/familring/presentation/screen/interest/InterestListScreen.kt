package com.familring.presentation.screen.interest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.dialog.OneButtonTextDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun InterestListRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    interestListViewModel: InterestListViewModel = hiltViewModel(),
) {
    val uiState by interestListViewModel.uiState.collectAsStateWithLifecycle()
    val selectedInterests = interestListViewModel.selectedInterests.collectAsLazyPagingItems()

    InterestListScreen(
        modifier = modifier,
        detailInterests = uiState.detailInterests,
        interestList = selectedInterests,
        getInterestDetails = interestListViewModel::getInterestDetails,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun InterestListScreen(
    modifier: Modifier = Modifier,
    detailInterests: List<Mission> = listOf(),
    interestList: LazyPagingItems<SelectedInterest>,
    getInterestDetails: (Long) -> Unit = {},
    popUpBackStack: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

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
                        text = "관심사 공유 목록",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "우리가 지금까지 이런 관심사들을 나눴어요",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
            ) {
                items(interestList.itemCount) { index ->
                    val item = interestList[index]
                    if (item != null) {
                        InterestListItem(
                            selectedInterest = item,
                            clickItem = { interestId ->
                                getInterestDetails(interestId)
                                showDialog = true
                            },
                        )
                    }
                }
            }
        }

        if (showDialog) {
            if (detailInterests.isEmpty()) {
                Dialog(
                    onDismissRequest = { showDialog = false },
                    properties =
                        DialogProperties(usePlatformDefaultWidth = false),
                ) {
                    OneButtonTextDialog(
                        text = "아무도 인증하지 않았어요",
                        buttonText = "확인",
                        onButtonClick = { showDialog = false },
                    )
                }
            } else {
                val pagerState = rememberPagerState(pageCount = { detailInterests.size })

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(color = Black.copy(alpha = 0.5f))
                            .noRippleClickable {
                                showDialog = false
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        HorizontalPager(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.55f)
                                    .clickable(enabled = false) {},
                            state = pagerState,
                            pageSpacing = 15.dp,
                            contentPadding = PaddingValues(horizontal = 35.dp),
                        ) { page ->
                            SharePagerItem(
                                imgUrl = detailInterests[page].missionImgUrl,
                                username = detailInterests[page].userNickname,
                                zodiacImg = detailInterests[page].profileImgUrl,
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "빈 화면을 터치하면 닫혀요!",
                            style = Typography.displaySmall.copy(fontSize = 18.sp),
                            color = White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InterestListItem(
    modifier: Modifier = Modifier,
    selectedInterest: SelectedInterest = SelectedInterest(),
    clickItem: (Long) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(
                    text = "${selectedInterest.index}번째 관심사",
                    style = Typography.displaySmall,
                    color = Gray01,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${selectedInterest.userNickname}의",
                        style = Typography.bodyLarge.copy(fontSize = 22.sp),
                        color = Black,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = selectedInterest.interest,
                        style = Typography.titleSmall,
                        color = Green02,
                    )
                }
            }
            Row(
                modifier =
                    Modifier
                        .wrapContentSize()
                        .noRippleClickable {
                            clickItem(selectedInterest.interestId)
                        },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "자세히보기",
                    style = Typography.displaySmall.copy(fontSize = 14.sp),
                    color = Gray01,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate),
                    contentDescription = "detail_navigate",
                    tint = Gray01,
                )
            }
        }
    }
}
