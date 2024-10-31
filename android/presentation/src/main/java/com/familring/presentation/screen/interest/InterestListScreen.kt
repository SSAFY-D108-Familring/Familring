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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.familring.domain.model.InterestCard
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
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
) {
    InterestListScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        interestList =
            listOf(
                InterestCard(
                    profileImage = R.drawable.img_smile_face,
                    nickname = "나갱이",
                    interest = "엘지트윈스",
                ),
                InterestCard(
                    profileImage = R.drawable.img_interest_heart,
                    nickname = "현지니",
                    interest = "지드래곤",
                ),
                InterestCard(
                    profileImage = R.drawable.img_no_share_face,
                    nickname = "승주니",
                    interest = "네코타츠나",
                ),
                InterestCard(
                    profileImage = R.drawable.img_wrapped_gift,
                    nickname = "엄망이",
                    interest = "필라테스",
                ),
                InterestCard(
                    profileImage = R.drawable.img_worried_face,
                    nickname = "아빵이",
                    interest = "헬스",
                ),
            ),
    )
}

@Composable
fun InterestListScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    interestList: List<InterestCard> = listOf(),
    clickItem: (Int) -> Unit = {},
) {
    val state = rememberLazyListState()
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
                state = state,
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(25.dp),
            ) {
                items(interestList.size) { index ->
                    val item = interestList[index]

                    InterestListItem(
                        index = index + 1,
                        nickname = item.nickname,
                        interest = item.interest,
                        clickItem = { index ->
                            // index 갖고 데이터 불러오기
                            showDialog = true
                        },
                    )
                }
            }
        }

        if (showDialog) {
            val data =
                listOf(
                    R.drawable.sample_img to "나갱이의 인증샷",
                    R.drawable.sample_img to "승주니의 인증샷",
                    R.drawable.sample_img to "현지니의 인증샷",
                    R.drawable.sample_img to "엄마미의 인증샷",
                    R.drawable.sample_img to "아빵이의 인증샷",
                )
            val pagerState = rememberPagerState(pageCount = { data.size })

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
                            imageUri = data[page].first,
                            username = data[page].second,
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

@Composable
fun InterestListItem(
    modifier: Modifier = Modifier,
    index: Int = 0,
    nickname: String = "",
    interest: String = "",
    clickItem: (Int) -> Unit = {},
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
                    text = "${index}번째 관심사",
                    style = Typography.displaySmall,
                    color = Gray01,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${nickname}의",
                        style = Typography.bodyLarge.copy(fontSize = 22.sp),
                        color = Black,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = interest,
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
                            clickItem(index) // 여기에 이제 관심사 id
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
