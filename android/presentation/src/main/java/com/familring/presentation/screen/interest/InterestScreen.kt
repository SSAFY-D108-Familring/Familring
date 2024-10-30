package com.familring.presentation.screen.interest

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.GrayBigTextField
import com.familring.presentation.component.RoundLongButton
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.component.TopAppBarNavigationType
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InterestRoute(
    modifier: Modifier,
    navigateToInterestList: () -> Unit,
) {
    InterestScreen(modifier = modifier)
}

@Composable
fun InterestScreen(
    modifier: Modifier = Modifier,
    navigateToInterestList: () -> Unit = {},
    isWrote: Boolean = false,
    isUpload: Boolean = false,
    interest: String = "",
    isShareDay: Boolean = true, // true: 인증샷 남기는 날, false: 작성하는 날
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
    shareImage: () -> Unit = {},
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
                navigationType = TopAppBarNavigationType.None,
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
            if (isShareDay) {
                ShareDayScreen(
                    isUpload = isUpload,
                    shareImage = shareImage,
                )
            } else {
                WriteDayScreen(
                    isWrote = isWrote,
                    interest = interest,
                    writeInterest = writeInterest,
                    editInterest = editInterest,
                )
            }
        }
    }
}

@Composable
fun WriteDayScreen(
    isWrote: Boolean = false,
    interest: String = "",
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
) {
    var interestKeyword by remember { mutableStateOf("") }
    var canEdit by remember { mutableStateOf(true) }
    var wrote by remember { mutableStateOf(isWrote) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    if (interest.isNotEmpty()) {
        interestKeyword = interest
        canEdit = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.interest_script_1),
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = stringResource(R.string.interest_script_recent),
                style = Typography.titleSmall.copy(fontSize = 22.sp),
                color = Green03,
            )
        }
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = stringResource(R.string.interest_script_2),
            style = Typography.bodyLarge.copy(fontSize = 22.sp),
            color = Black,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.45f)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_interest_heart),
                contentDescription = "img_interest_heart",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = stringResource(R.string.my_recent_interest),
                style = Typography.displayMedium.copy(fontSize = 22.sp),
                color = Gray02,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            GrayBigTextField(
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester),
                keyword = interestKeyword,
                onValueChange = {
                    interestKeyword = it
                },
                placeHolder = stringResource(R.string.interest_keyword_placeholder),
                focusManager = focusManager,
                maxLength = 10,
                enabled = canEdit,
                onDone = {
                    if (interestKeyword.isNotEmpty()) {
                        if (wrote) {
                            editInterest(interestKeyword) // 관심사 수정
                        } else {
                            writeInterest(interestKeyword)
                            wrote = true
                        }
                        canEdit = false
                    }
                },
            )
            if (!wrote) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.interest_ten_word),
                    style = Typography.displayMedium.copy(fontSize = 16.sp),
                    color = Gray02,
                )
            } else {
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Text(
                    modifier =
                        Modifier.noRippleClickable {
                            canEdit = true
                            scope.launch {
                                delay(50)
                                focusRequester.requestFocus()
                            }
                        },
                    text = stringResource(R.string.interest_edit_btn),
                    style = Typography.displayMedium.copy(fontSize = 18.sp),
                    color = Gray02,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    }
}

@Composable
fun ShareDayScreen(
    isUpload: Boolean = false,
    shareImage: () -> Unit = {},
) {
    if (!isUpload) {
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val singlePhotoPickerLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    selectedImageUri = uri
                    shareImage()
                },
            )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.interest_week_select),
                    style = Typography.bodyLarge.copy(fontSize = 22.sp),
                    color = Black,
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "엘지 트윈스!",
                    style = Typography.titleSmall.copy(fontSize = 22.sp),
                    color = Green03,
                )
            }
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = stringResource(R.string.interest_share_script),
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.45f)
                            .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.img_no_share_face),
                    contentDescription = "img_interest_heart",
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Text(
                    text = stringResource(R.string.interest_no_share_script),
                    style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    color = Black,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.interest_upload_script),
                        style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "7",
                        style = Typography.titleMedium.copy(fontSize = 22.sp),
                        color = Green02,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.interest_upload_script2),
                        style = Typography.bodyLarge.copy(fontSize = 20.sp),
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))
                RoundLongButton(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    text = "인증샷 업로드하기",
                    textColor = White,
                    backgroundColor = Green02,
                    enabled = true,
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                )
            }
        }
    } else {
        val items =
            listOf(
                R.drawable.sample_img to "나갱이의 인증샷",
                R.drawable.sample_img to "승주니의 인증샷",
                R.drawable.sample_img to "현지니의 인증샷",
                R.drawable.sample_img to "엄마미의 인증샷",
                R.drawable.sample_img to "아빵이의 인증샷",
            )
        val pagerState = rememberPagerState(pageCount = { items.size })

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.interest_week_select),
                    style = Typography.bodyLarge.copy(fontSize = 22.sp),
                    color = Black,
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "엘지 트윈스!",
                    style = Typography.titleSmall.copy(fontSize = 22.sp),
                    color = Green03,
                )
            }
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "우리 가족의 인증샷을 구경해 보세요",
                style = Typography.bodyLarge.copy(fontSize = 22.sp),
                color = Black,
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.08f))
                HorizontalPager(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .align(Alignment.CenterHorizontally),
                    state = pagerState,
                    pageSpacing = 15.dp,
                    contentPadding = PaddingValues(horizontal = 35.dp),
                ) { page ->
                    SharePagerItem(
                        imageUri = items[page].first,
                        username = items[page].second,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "4명이 관심사 공유에 참여했어요!",
                    style = Typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier =
                        Modifier.noRippleClickable {
                        },
                    text = "선정되지 못한 관심사 알아보기",
                    style = Typography.bodyMedium,
                    color = Gray01,
                    textDecoration = TextDecoration.Underline,
                )
                Spacer(modifier = Modifier.height(35.dp))
            }
        }
    }
}

@Preview
@Composable
fun InterestScreenPreview() {
    InterestScreen(
        isWrote = true,
        interest = "엘지 트윈스",
    )
}
