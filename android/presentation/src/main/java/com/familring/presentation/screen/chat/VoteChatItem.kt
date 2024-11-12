package com.familring.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.presentation.R
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Blue02
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography

@Composable
fun VoteChatItem(
    isOther: Boolean,
    title: String,
    select: String,
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
                modifier =
                    Modifier
                        .height(IntrinsicSize.Max),
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(IntrinsicSize.Max)
                            .fillMaxHeight(),
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
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .padding(end = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        style = Typography.titleSmall.copy(fontSize = 13.sp),
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = if (select == "찬성") {
                            painterResource(id = R.drawable.img_agree)
                        } else {
                            painterResource(
                                id = R.drawable.img_disagree,
                            )
                        },
                        contentDescription = "agree",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = select,
                        style = Typography.titleSmall.copy(fontSize = 15.sp),
                        color = if (select == "찬성") Blue02 else Red01,
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier =
                    Modifier
                        .height(IntrinsicSize.Max),
            ) {
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
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .padding(start = 3.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = nickname,
                        style = Typography.headlineSmall.copy(fontSize = 13.sp),
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Column(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .padding(start = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = title,
                            style = Typography.titleSmall.copy(fontSize = 13.sp),
                            color = Gray01,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            modifier = Modifier.size(80.dp),
                            painter =
                                if (select == "찬성") {
                                    painterResource(id = R.drawable.img_agree)
                                } else {
                                    painterResource(
                                        id = R.drawable.img_disagree,
                                    )
                                },
                            contentDescription = "agree",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = select,
                            style = Typography.titleSmall.copy(fontSize = 15.sp),
                            color = if (select == "찬성") Blue02 else Red01,
                        )
                    }
                }
                Box(
                    modifier =
                        Modifier
                            .width(IntrinsicSize.Max)
                            .fillMaxHeight(),
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

@Preview(showBackground = true)
@Composable
fun VoteChatItemPreview() {
    VoteChatItem(
        isOther = true,
        title = "오늘 저녁 치킨 어때유?",
        select = "찬성",
        time = "15:00",
        unReadMembers = "2",
        nickname = "나갱",
        color = "0xFFD9D9D9",
        profileImg = "",
    )
}
