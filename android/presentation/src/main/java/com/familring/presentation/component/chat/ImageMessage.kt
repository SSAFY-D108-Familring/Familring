package com.familring.presentation.component.chat

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.familring.domain.model.Profile
import com.familring.presentation.component.ZodiacBackgroundProfile
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography

@Composable
fun ImageMessage(
    isOther: Boolean,
    filePath: String,
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
                    modifier = Modifier.width(IntrinsicSize.Max),
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
                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .clip(shape = RoundedCornerShape(10.dp)),
                    model = filePath,
                    contentDescription = "chat_img",
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row {
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
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Spacer(modifier = Modifier.width(7.dp))
                    Column {
                        Text(
                            text = nickname,
                            style = Typography.headlineSmall.copy(fontSize = 13.sp),
                            color = Gray01,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        AsyncImage(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.65f)
                                    .clip(shape = RoundedCornerShape(10.dp)),
                            model = filePath,
                            contentDescription = "chat_img",
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.padding(start = 2.dp),
                                text = if (unReadMembers != "0") unReadMembers else "",
                                style = Typography.titleSmall.copy(fontSize = 10.sp),
                                color = Green02,
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
}

@Preview(showBackground = true)
@Composable
fun ImageMessagePreview() {
    ImageMessage(
        isOther = true,
        filePath = "",
        time = "13:00",
        unReadMembers = "3",
        nickname = "나갱",
        profileImg = "",
        color = "0xFFD9D9D9",
    )
}
