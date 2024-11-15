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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
import com.familring.domain.request.UserEmotionRequest
import com.familring.presentation.R
import com.familring.presentation.component.LoveMention
import com.familring.presentation.component.dialog.LoadingDialog
import com.familring.presentation.component.tutorial.TreeExplanation
import com.familring.presentation.screen.mypage.roleToWord
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Green04
import com.familring.presentation.theme.Green05
import com.familring.presentation.theme.Green06
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

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
    val homeEvent by viewModel.homeEvent.collectAsStateWithLifecycle(initialValue = HomeEvent.None)

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (val state = homeState) {
        is HomeState.Loading -> {
            LoadingDialog(loadingMessage = "홈 화면을 불러오고 있어요...")
        }

        is HomeState.Success -> {
            HomeScreen(
                modifier = modifier,
                familyMembers = state.familyMembers,
                familyInfo = state.familyInfo,
                navigateToNotification = navigateToNotification,
                navigateToTimeCapsule = navigateToTimeCapsule,
                navigateToInterest = navigateToInterest,
                navigateToMyPage = navigateToMyPage,
                showSnackBar = showSnackBar,
                homeEvent = homeEvent,
                viewModel = viewModel,
            )
        }

        is HomeState.Error -> {
            HomeScreen(
                modifier = modifier,
                navigateToNotification = navigateToNotification,
                navigateToTimeCapsule = navigateToTimeCapsule,
                navigateToInterest = navigateToInterest,
                showSnackBar = showSnackBar,
                homeEvent = homeEvent,
                viewModel = viewModel,
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
    homeEvent: HomeEvent,
    viewModel: HomeViewModel,
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

    LaunchedEffect(homeEvent) {
        when (homeEvent) {
            is HomeEvent.Success -> {
                showSnackBar("알림을 성공적으로 전송했습니다")
            }

            is HomeEvent.UpdateSuccess -> {
                showSnackBar("나의 현재 기분이 변경되었어요!")
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserId()
    }

    val father = familyMembers.find { it.userRole == "F" }
    val mother = familyMembers.find { it.userRole == "M" }
    val children = familyMembers.filter { it.userRole == "S" || it.userRole == "D" }
    val currentUserId by viewModel.myUserId.collectAsStateWithLifecycle()

    var showTreeExplanation by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showLoveMention by remember { mutableStateOf(false) }
    var showEditEmotion by remember { mutableStateOf(false) }

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
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier =
                        Modifier
                            .noRippleClickable { showTreeExplanation = true }
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
                            painter =
                                painterResource(
                                    id =
                                        if (progress > 75f) {
                                            R.drawable.img_tree_status_four
                                        } else if (progress > 50f) {
                                            R.drawable.img_tree_status_three
                                        } else if (progress > 25f) {
                                            R.drawable.img_tree_status_two
                                        } else {
                                            R.drawable.img_tree_status_one
                                        },
                                ),
                            contentDescription = "tree_img",
                        )
                        Column {
                            Text(
                                modifier = Modifier.padding(start = 15.dp),
                                text = "우리 가족의 나무는 지금...",
                                style = Typography.displayMedium,
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                modifier = Modifier.padding(start = 15.dp),
                                text =
                                    if (progress > 75f) {
                                        "초록초록 \uD83E\uDD70"
                                    } else if (progress > 50f) {
                                        "파릇파릇 \uD83D\uDE0A"
                                    } else if (progress > 25f) {
                                        "무럭무럭 \uD83D\uDE42"
                                    } else {
                                        "민둥맨둥 \uD83D\uDE30"
                                    },
                                style = Typography.titleLarge.copy(fontSize = 24.sp),
                                color = Green02,
                            )
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp)
                                        .padding(top = 8.dp),
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 2.dp),
                                    contentAlignment = Alignment.CenterEnd,
                                ) {
                                    Text(
                                        text = "100",
                                        style = Typography.displaySmall.copy(fontSize = 8.sp),
                                        color = Gray02,
                                    )
                                }
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
                                                    if (progress > 75f) {
                                                        Green02
                                                    } else if (progress > 50f) {
                                                        Green03
                                                    } else if (progress > 25f) {
                                                        Green04
                                                    } else {
                                                        Green05
                                                    },
                                                ).animateContentSize(),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 2.dp),
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
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(23.dp),
                                        spotColor = Gray01,
                                        ambientColor = Gray01,
                                    ).background(color = Gray04, shape = RoundedCornerShape(23.dp))
                                    .noRippleClickable { navigateToTimeCapsule() },
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 15.dp)
                                        .padding(top = 15.dp),
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
                                        modifier =
                                            Modifier
                                                .fillMaxWidth(0.6f)
                                                .aspectRatio(1f)
                                                .padding(bottom = 8.dp),
                                    )
                                }
                            }
                        }
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .shadow(
                                        elevation = 7.dp,
                                        shape = RoundedCornerShape(23.dp),
                                        spotColor = Black,
                                        ambientColor = Gray01,
                                    ).background(color = Green02, shape = RoundedCornerShape(23.dp))
                                    .noRippleClickable { navigateToInterest() },
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 15.dp)
                                        .padding(top = 15.dp),
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
                                        modifier =
                                            Modifier
                                                .fillMaxWidth(0.55f)
                                                .aspectRatio(1f)
                                                .padding(bottom = 15.dp),
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (mother != null && father == null) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_heart),
                                contentDescription = "heart_img",
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            FamilyCard(
                                user = mother,
                                onCardClick = {
                                    if (mother.userId != currentUserId) {
                                        selectedUser = mother
                                        showLoveMention = true
                                    } else {
                                        showEditEmotion = true
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Image(
                                painter = painterResource(id = R.drawable.img_heart),
                                contentDescription = "heart_img",
                            )
                        }
                    } else if (mother == null && father != null) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_heart),
                                contentDescription = "heart_img",
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            FamilyCard(
                                user = father,
                                onCardClick = {
                                    if (father.userId != currentUserId) {
                                        selectedUser = father
                                        showLoveMention = true
                                    } else {
                                        showEditEmotion = true
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Image(
                                painter = painterResource(id = R.drawable.img_heart),
                                contentDescription = "heart_img",
                            )
                        }
                    } else if (mother != null && father != null) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FamilyCard(
                                user = mother,
                                onCardClick = {
                                    if (mother.userId != currentUserId) {
                                        selectedUser = mother
                                        showLoveMention = true
                                    } else {
                                        showEditEmotion = true
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Image(
                                painter = painterResource(id = R.drawable.img_heart),
                                contentDescription = "heart_img",
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            FamilyCard(
                                user = father,
                                onCardClick = {
                                    if (father.userId != currentUserId) {
                                        selectedUser = father
                                        showLoveMention = true
                                    } else {
                                        showEditEmotion = true
                                    }
                                },
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                LazyRow(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(children.size) { index ->
                        FamilyCard(
                            user = children[index],
                            onCardClick = {
                                if (children[index].userId != currentUserId) {
                                    selectedUser = children[index]
                                    showLoveMention = true
                                } else {
                                    showEditEmotion = true
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    if (showLoveMention && selectedUser != null) {
        LoveMention(
            user = selectedUser!!,
            onClose = {
                showLoveMention = false
                selectedUser = null
            },
            onSend = { content ->
                viewModel.sendMentionNotification(
                    selectedUser!!.userId,
                    content,
                )
            },
        )
    }

    if (showEditEmotion) {
        EmotionUpdateScreen(
            onClose = { showEditEmotion = false },
            clickEmotion = { emotion ->
                viewModel.updateEmotion(UserEmotionRequest(userEmotion = emotion))
            },
        )
    }

    if (showTreeExplanation) {
        TreeExplanation(onClose = { showTreeExplanation = false })
    }
}

@Composable
fun FamilyCard(
    user: User,
    onCardClick: (Long) -> Unit = {},
) {
    var contentHeight by remember { mutableIntStateOf(0) } // 콘텐츠 높이 저장
    ElevatedCard(
        modifier =
            Modifier
                .width(with(LocalDensity.current) { contentHeight.toDp() })
                .noRippleClickable {
                    onCardClick(user.userId)
                },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Green06),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier =
                    Modifier
                        .onSizeChanged { size -> contentHeight = size.height } // 콘텐츠 높이 추적
                        .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = roleToWord(user.userRole),
                        style = Typography.displaySmall.copy(fontSize = 12.sp, color = Gray02),
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = user.userNickname,
                        style = Typography.titleLarge.copy(fontSize = 15.sp, color = Black),
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                AsyncImage(
                    model = user.userZodiacSign,
                    modifier =
                        Modifier
                            .size(50.dp)
                            .aspectRatio(1f),
                    contentDescription = "user zodiac img",
                )
                Spacer(modifier = Modifier.height(13.dp))
                Text(
                    user.userEmotion.ifEmpty {
                        "평범해요 🙂"
                    },
                    style = Typography.displaySmall.copy(fontSize = 14.sp, color = Black),
                )
            }
        }
    }
}

@Composable
fun EmptyCard() {
    var contentHeight by remember { mutableIntStateOf(0) } // 콘텐츠 높이 저장
    Box {
        ElevatedCard(
            modifier =
                Modifier
                    .width(with(LocalDensity.current) { contentHeight.toDp() }),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Green06),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier =
                        Modifier
                            .onSizeChanged { size -> contentHeight = size.height } // 콘텐츠 높이 추적
                            .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "우리가좍",
                        style = Typography.titleLarge.copy(fontSize = 15.sp, color = Black),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    AsyncImage(
                        model = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/토끼.png",
                        modifier =
                            Modifier
                                .size(50.dp)
                                .aspectRatio(1f),
                        contentDescription = "user zodiac img",
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    Text(
                        text = "평범해요 🙂",
                        style = Typography.displaySmall.copy(fontSize = 14.sp, color = Black),
                    )
                }
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
//    HomeScreen(
//        familyMembers = emptyList(),
//        navigateToNotification = {},
//        navigateToTimeCapsule = {},
//        navigateToInterest = {},
//        showSnackBar = {},
//        homeEvent = HomeEvent.None,
//        viewModel = hiltViewModel(),
//    )

    FamilyCard(user = User())
}
