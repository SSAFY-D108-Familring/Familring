package com.familring.presentation.screen.interest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.interest.InterestCard
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun OtherInterestRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
) {
    OtherInterestScreen(
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
fun OtherInterestScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    interestList: List<InterestCard> = listOf(),
) {
    val state = rememberLazyGridState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "관심사 알아보기",
                        color = Black,
                        style = Typography.headlineMedium.copy(fontSize = 22.sp),
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "우리 가족은 요즘 이런 것들에 관심이 있어요!",
                style = Typography.bodyLarge,
                color = Gray01,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            LazyVerticalGrid(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                columns = GridCells.Fixed(2),
                state = state,
            ) {
                items(interestList.size) { index ->
                    val item = interestList[index]
                    InterestCardItem(
                        modifier = Modifier.padding(10.dp),
                        profileImage = item.profileImage,
                        nickname = item.nickname,
                        interest = item.interest,
                    )
                }
            }
        }
    }
}
