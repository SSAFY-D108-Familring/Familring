package com.familring.presentation.screen.interest

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.util.toMultiPart
import com.familring.presentation.R
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.familring.presentation.util.toFile
import okhttp3.MultipartBody

@Composable
fun ShareDayScreen(
    isUpload: Boolean = false,
    selectedInterest: SelectedInterest = SelectedInterest(),
    leftMissionPeriod: Int = 0,
    missions: List<Mission> = emptyList(),
    shareImage: (MultipartBody.Part?) -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
) {
    val context = LocalContext.current

    if (!isUpload) {
        val singlePhotoPickerLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    uri?.let {
                        shareImage(uri.toFile(context = context).toMultiPart("image"))
                    }
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
                    text = "${selectedInterest.interest}!",
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
                        text = leftMissionPeriod.toString(),
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
        val pagerState = rememberPagerState(pageCount = { missions.size })

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
                    text = "${selectedInterest.interest}!",
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
                        imgUrl = missions[page].missionImgUrl,
                        username = missions[page].userNickname,
                        zodiacImg = missions[page].profileImgUrl,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "${missions.size}명이 관심사 공유에 참여했어요!",
                    style = Typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier =
                        Modifier.noRippleClickable {
                            navigateToOtherInterest()
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
