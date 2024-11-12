package com.familring.presentation.component.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.presentation.R
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Blue01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Pink01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.theme.Yellow01
import com.familring.presentation.util.noRippleClickable

@Composable
fun VoteMessage(
    isOther: Boolean,
    title: String,
    onAgree: () -> Unit = {},
    onDisagree: () -> Unit = {},
    time: String,
    unReadMembers: String,
    nickname: String = "",
    profileImg: String = "",
    color: String = "",
) {
    if (!isOther) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(IntrinsicSize.Max)
                            .width(IntrinsicSize.Max),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Column {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 2.dp),
                            text = if (unReadMembers != "0") unReadMembers else "",
                            style = Typography.titleSmall.copy(fontSize = 10.sp),
                            color = Green02,
                            textAlign = TextAlign.End,
                        )
                        Text(
                            text = time,
                            style = Typography.bodySmall.copy(fontSize = 12.sp),
                            color = Gray02,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(7.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .clip(shape = RoundedCornerShape(13.dp))
                            .background(color = White)
                            .border(width = 1.dp, color = Gray03, shape = RoundedCornerShape(13.dp)),
                ) {
                    Column(modifier = Modifier.wrapContentSize()) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .background(color = Yellow01),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopStart,
                            ) {
                                Text(
                                    modifier = Modifier.padding(10.dp),
                                    text = "투표가 올라왔어요!",
                                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd,
                            ) {
                                Image(
                                    modifier = Modifier.padding(bottom = 5.dp, end = 3.dp),
                                    painter = painterResource(id = R.drawable.img_raise_hand),
                                    contentDescription = "raise_hand",
                                )
                            }
                        }
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(color = White),
                        ) {
                            Text(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 30.dp),
                                text = title,
                                style = Typography.displaySmall.copy(fontSize = 18.sp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.wrapContentHeight()) {
                ZodiacBackgroundProfile(
                    modifier = Modifier.padding(top = 5.dp),
                    profile =
                        Profile(
                            nickname = nickname,
                            zodiacImgUrl = profileImg,
                            backgroundColor = color,
                        ),
                    size = 36,
                    paddingValue = 5,
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column {
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = nickname,
                        style = Typography.headlineSmall.copy(fontSize = 13.sp),
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(0.6f)
                                .clip(shape = RoundedCornerShape(13.dp))
                                .background(color = White)
                                .border(
                                    width = 1.dp,
                                    color = Gray03,
                                    shape = RoundedCornerShape(13.dp),
                                ),
                    ) {
                        Column(modifier = Modifier.wrapContentSize()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .background(color = Yellow01),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopStart,
                                ) {
                                    Text(
                                        modifier = Modifier.padding(10.dp),
                                        text = "투표가 올라왔어요!",
                                        style = Typography.titleSmall.copy(fontSize = 16.sp),
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomEnd,
                                ) {
                                    Image(
                                        modifier = Modifier.padding(bottom = 5.dp, end = 3.dp),
                                        painter = painterResource(id = R.drawable.img_raise_hand),
                                        contentDescription = "raise_hand",
                                    )
                                }
                            }
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .background(color = White),
                            ) {
                                Column(modifier = Modifier.wrapContentSize()) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = title,
                                        style = Typography.displaySmall.copy(fontSize = 18.sp),
                                        textAlign = TextAlign.Center,
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp),
                                    ) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .weight(1f)
                                                    .noRippleClickable { onDisagree() }
                                                    .clip(shape = RoundedCornerShape(5.dp))
                                                    .background(color = Pink01),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                text = "반대",
                                                style = Typography.headlineSmall.copy(fontSize = 14.sp),
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier =
                                                Modifier
                                                    .weight(1f)
                                                    .noRippleClickable { onAgree() }
                                                    .clip(shape = RoundedCornerShape(5.dp))
                                                    .background(color = Blue01),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                text = "찬성",
                                                style = Typography.headlineSmall.copy(fontSize = 14.sp),
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(7.dp))
                Box(
                    modifier =
                        Modifier
                            .width(IntrinsicSize.Max)
                            .align(Alignment.Bottom),
                ) {
                    Column {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 2.dp),
                            text = if (unReadMembers != "0") unReadMembers else "",
                            style = Typography.titleSmall.copy(fontSize = 10.sp),
                            color = Green02,
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = time,
                            style = Typography.bodySmall.copy(fontSize = 12.sp),
                            color = Gray02,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MyVoteMessagePreview() {
    VoteMessage(
        isOther = true,
        title = "오늘 저녁 치킨 어때유?",
        unReadMembers = "3",
        time = "15:00",
        nickname = "나갱",
        profileImg = "",
        color = "0xFFD9D9D9",
    )
}
