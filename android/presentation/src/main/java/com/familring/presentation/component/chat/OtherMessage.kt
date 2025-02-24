package com.familring.presentation.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Brown01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun OtherMessage(
    nickname: String,
    profileImg: String,
    color: String,
    message: String,
    time: String,
    unReadMembers: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
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
                Column(modifier = Modifier.wrapContentSize()) {
                    Text(
                        text = nickname,
                        style = Typography.headlineSmall.copy(fontSize = 13.sp),
                        color = Gray01,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier =
                            Modifier
                                .background(
                                    color = Brown01,
                                    shape =
                                        RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 15.dp,
                                            bottomStart = 15.dp,
                                            bottomEnd = 15.dp,
                                        ),
                                ).padding(horizontal = 13.dp, vertical = 10.dp),
                        text = message,
                        style = Typography.bodyLarge,
                        color = White,
                    )
                }
            }
            Spacer(modifier = Modifier.width(7.dp))
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column {
                    if (unReadMembers != "0") {
                        Text(
                            text = unReadMembers,
                            style = Typography.titleSmall.copy(fontSize = 10.sp),
                            color = Green02,
                            modifier = Modifier.padding(start = 2.dp),
                        )
                    }
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

@Preview
@Composable
fun OtherMessagePreview() {
    OtherMessage(
        nickname = "dd",
        profileImg = "",
        color = "0xFFD9D9D9",
        message = "asdfasdfdsfsfdlkjldkjdlkjdlkdjldkjldkjdlkjdlkjdlkjdddfdfdfdf",
        time = "15:00",
        unReadMembers = "3",
    )
}
