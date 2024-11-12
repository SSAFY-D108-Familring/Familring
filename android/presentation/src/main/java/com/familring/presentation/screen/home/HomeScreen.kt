package com.familring.presentation.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.User
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Green04
import com.familring.presentation.theme.Green06
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import timber.log.Timber

@Composable
fun HomeRoute(
    modifier: Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit,
    navigateToTimeCapsule: () -> Unit,
    navigateToInterest: () -> Unit,
    navigateToMyPage: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (val state = homeState) {
        is HomeState.Loading -> {
            Timber.tag("nakyung").d("홈화면 로딩중")
        }

        is HomeState.Success -> {
            Timber.tag("nakyung").d("홈화면 그려짐")
            HomeScreen(
                modifier = modifier,
                familyMembers = state.familyMembers,
                familyInfo = state.familyInfo,
                navigateToNotification = navigateToNotification,
                navigateToTimeCapsule = navigateToTimeCapsule,
                navigateToInterest = navigateToInterest,
                navigateToMyPage = navigateToMyPage,
                showSnackBar = showSnackBar,
            )
        }

        is HomeState.Error -> {
            HomeScreen(
                modifier = modifier,
                navigateToNotification = navigateToNotification,
                navigateToTimeCapsule = navigateToTimeCapsule,
                navigateToInterest = navigateToInterest,
                showSnackBar = showSnackBar,
            )
            showSnackBar(state.errorMessage)
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    familyMembers: List<User> = listOf(),
    familyInfo: FamilyInfo = FamilyInfo(),
    navigateToNotification: () -> Unit = {},
    navigateToTimeCapsule: () -> Unit = {},
    navigateToInterest: () -> Unit = {},
    navigateToMyPage: () -> Unit = {},
    showSnackBar: (String) -> Unit = {},
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    val size by animateFloatAsState(
        targetValue = progress / 100f,
        tween(delayMillis = 200, durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(Unit) {
        progress = familyInfo.familyCommunicationStatus.toFloat().coerceIn(0f, 100f)
    }

    val father = familyMembers.find { it.userRole == "F" }
    val mother = familyMembers.find { it.userRole == "M" }
    val children = familyMembers.filter { it.userRole == "S" || it.userRole == "D" }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mainlogo),
                        contentDescription = "Main_logo_img",
                    )
                    Row(modifier = Modifier.padding(top = 5.dp)) {
                        Image(
                            modifier =
                                Modifier
                                    .size(24.dp)
                                    .noRippleClickable {
                                        navigateToNotification()
                                    },
                            painter = painterResource(id = R.drawable.img_home_notification),
                            contentDescription = "home notification img",
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier =
                                Modifier
                                    .size(24.dp)
                                    .noRippleClickable {
                                        navigateToMyPage()
                                    },
                            painter = painterResource(id = R.drawable.img_profile_circle),
                            contentDescription = "profile circle img",
                        )
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row {
                    Spacer(
                        modifier =
                            Modifier
                                .width(4.dp)
                                .height(19.dp)
                                .background(color = Green01, shape = RoundedCornerShape(4.dp)),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "나무로 알아보는 우리의 소통 상태",
                        style = Typography.headlineSmall.copy(fontSize = 18.sp),
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier =
                        Modifier
                            .border(
                                width = 1.dp,
                                color = Gray03,
                                shape = RoundedCornerShape(15.dp),
                            ).padding(vertical = 15.dp),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            modifier = Modifier.size(147.dp),
                            painter = painterResource(id = R.drawable.img_tree_status_two),
                            contentDescription = "tree_img",
                        )
                        Column {
                            Text(
                                modifier = Modifier.padding(start = 15.dp),
                                text =
                                    if (progress > 69f) { // 69% 초과
                                        "열심히 하셧고\n축하하고 ㅎㅎ🎄"
                                    } else if (progress > 29f) { // 29% 초과
                                        "소통을 조금만 더\n해주시고 ㅋㅋ\uD83C\uDF84"
                                    } else { // 29% 이하
                                        "소통이 조금 더\n필요해요\uD83D\uDE30"
                                    },
                                style = Typography.titleLarge.copy(fontSize = 24.sp),
                                color = Green02,
                            )
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp)
                                        .padding(top = 12.dp),
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(15.dp),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(9.dp))
                                                .background(Gray02),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth(size)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(9.dp))
                                                .background(
                                                    if (progress > 69f) {
                                                        Green02
                                                    } else if (progress > 29f) {
                                                        Green03
                                                    } else {
                                                        Green04
                                                    },
                                                ).animateContentSize(),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth(size)
                                                .padding(end = 0.dp),
                                        contentAlignment = Alignment.CenterEnd,
                                    ) {
                                        Text(
                                            text = "${progress.toInt()}",
                                            style = Typography.displaySmall.copy(fontSize = 8.sp),
                                            color = Color.Black,
                                        )
                                    }

                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(end = 0.dp),
                                        contentAlignment = Alignment.CenterEnd,
                                    ) {
                                        Text(
                                            text = "100",
                                            style = Typography.displaySmall.copy(fontSize = 8.sp),
                                            color = Gray02,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 25.dp),
                ) {
                    Row {
                        Spacer(
                            modifier =
                                Modifier
                                    .width(4.dp)
                                    .height(19.dp)
                                    .background(color = Green01, shape = RoundedCornerShape(4.dp)),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = "서로를 알아가는 시간 가져 보기",
                            style = Typography.headlineSmall.copy(fontSize = 18.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Surface(
                            shape = RoundedCornerShape(23.dp),
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(end = 6.dp)
                                    .padding(top = 12.dp)
                                    .noRippleClickable { navigateToTimeCapsule() },
                            color = Gray04,
                            shadowElevation = 4.dp,
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 15.dp)
                                        .padding(top = 21.dp),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        text = "타임캡슐",
                                        style = Typography.titleSmall.copy(fontSize = 26.sp),
                                    )
                                    Spacer(modifier = Modifier.fillMaxSize(0.02f))
                                    Text(
                                        text = "나중에 열어보는 속마음",
                                        style = Typography.displaySmall.copy(fontSize = 13.sp),
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.BottomEnd,
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_timecap),
                                        contentDescription = "img_timecap",
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )
                                }
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(23.dp),
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(start = 6.dp)
                                    .padding(top = 12.dp)
                                    .noRippleClickable { navigateToInterest() },
                            color = Green02,
                            shadowElevation = 12.dp,
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 15.dp)
                                        .padding(top = 21.dp),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        text = "관심사 공유",
                                        style = Typography.titleSmall.copy(fontSize = 26.sp),
                                        color = White,
                                    )
                                    Spacer(modifier = Modifier.fillMaxSize(0.02f))
                                    Text(
                                        text = "함께 즐기는 서로의 취미",
                                        style = Typography.displaySmall.copy(fontSize = 13.sp),
                                        color = White,
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.BottomEnd,
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_interest),
                                        contentDescription = "img_interest",
                                        modifier = Modifier.padding(bottom = 15.dp),
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row {
                    Spacer(
                        modifier =
                            Modifier
                                .width(4.dp)
                                .height(19.dp)
                                .background(color = Green01, shape = RoundedCornerShape(4.dp)),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "오늘 우리 가족의 기분은?",
                        style = Typography.headlineSmall.copy(fontSize = 18.sp),
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (mother != null) {
                        FamilyCard(mother)
                    } else {
                        EmptyCard()
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.img_heart),
                        contentDescription = "heart_img",
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    if (father != null) {
                        FamilyCard(father)
                    } else {
                        EmptyCard()
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(children.size) { index ->
                    FamilyCard(children[index])
                }
            }
        }
    }
}

@Composable
fun FamilyCard(user: User) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.width(124.dp).aspectRatio(1f),
    ) {
        Column(
            modifier =
                Modifier
                    .aspectRatio(1f)
                    .background(color = Green06)
                    .padding(horizontal = 22.dp)
                    .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (user.userRole.equals("M")) {
                    Text(
                        text = "엄마",
                        style = Typography.displaySmall.copy(fontSize = 12.sp, color = Gray02),
                    )
                } else if (user.userRole.equals("F")) {
                    Text(
                        text = "아빠",
                        style = Typography.displaySmall.copy(fontSize = 12.sp, color = Gray02),
                    )
                } else if (user.userRole.equals("D")) {
                    Text(
                        text = "딸",
                        style = Typography.displaySmall.copy(fontSize = 12.sp, color = Gray02),
                    )
                } else {
                    Text(
                        text = "아들",
                        style = Typography.displaySmall.copy(fontSize = 12.sp, color = Gray02),
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = user.userNickname,
                    style = Typography.titleLarge.copy(fontSize = 15.sp, color = Black),
                )
            }

            Spacer(modifier = Modifier.fillMaxSize(0.01f))
            AsyncImage(
                model = user.userZodiacSign,
                modifier =
                    Modifier
                        .size(51.dp)
                        .aspectRatio(1f),
                contentDescription = "user zodiac img",
            )

            Spacer(
                modifier = Modifier.height(13.dp),
            )
            Text(
                user.userEmotion.ifEmpty {
                    "평범해요 🙂"
                },
                style = Typography.displaySmall.copy(fontSize = 14.sp, color = Black),
            )
        }
    }
}

@Composable
fun EmptyCard() {
    Box {
        ElevatedCard(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .background(color = Green06)
                        .padding(horizontal = 22.dp)
                        .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "우리가좍",
                    style = Typography.titleLarge.copy(fontSize = 15.sp),
                    modifier = Modifier.alpha(0.3f),
                )
                Spacer(modifier = Modifier.fillMaxSize(0.01f))
                Image(
                    painter = painterResource(id = R.drawable.img_chicken),
                    contentDescription = "chicken_img",
                    modifier =
                        Modifier
                            .size(65.dp)
                            .aspectRatio(1f)
                            .alpha(0.3f),
                )
                Spacer(
                    modifier = Modifier.height(15.dp),
                )
                Text(
                    text = "평범해요 \uD83D\uDE42",
                    style = Typography.displaySmall.copy(fontSize = 14.sp),
                    modifier = Modifier.alpha(0.3f),
                )
            }
        }
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "아직 가족이 등록되지\n않았어요!",
                style = Typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        familyMembers = emptyList(),
        navigateToNotification = {},
        navigateToTimeCapsule = {},
        navigateToInterest = {},
        showSnackBar = {},
    )
}
