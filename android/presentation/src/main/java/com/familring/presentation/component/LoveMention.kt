package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.Profile
import com.familring.domain.model.User
import com.familring.presentation.component.button.RoundLongButton
import com.familring.presentation.component.textfield.CustomTextField
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun LoveMention(
    user: User,
    onClose: () -> Unit = {},
    onSend: (String) -> Unit,
) {
    var content by remember { mutableStateOf("") }
    var maxLength = 20
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black.copy(alpha = 0.4f),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .noRippleClickable { onClose() },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(enabled = false) {},
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = White)
                            .padding(vertical = 25.dp, horizontal = 12.dp),
                ) {
                    Row {
                        Text(
                            modifier = Modifier.padding(start = 13.dp),
                            text = user.userNickname, // 여기 유저 닉네임으로 바꾸기
                            color = Green02,
                            style = Typography.titleSmall.copy(fontSize = 22.sp),
                        )
                        Text(
                            text = "에게 사랑의 한마디 \uD83D\uDC9D",
                            color = Black,
                            style = Typography.titleSmall.copy(fontSize = 22.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "알림으로 사랑을 전해 드릴게요",
                        color = Gray01,
                        style = Typography.bodyMedium.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        ZodiacBackgroundProfile(
                            size = 130,
                            profile =
                                Profile(

                                    zodiacImgUrl = user.userZodiacSign, // 프로필 사진 URL
                                    backgroundColor = user.userColor, // 유저 배경 색
                                ),
                            paddingValue = 20
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "${content.length}/$maxLength",
                        style = Typography.bodySmall.copy(fontSize = 12.sp),
                        color = Gray02,
                        modifier = Modifier.align(Alignment.End).padding(end = 16.dp),
                    )
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = content,
                        placeHolder = "한마디를 입력해주세요",
                        onValueChanged = {
                            if (it.length <= maxLength) {
                                content = it
                            }
                        },
                        focusManager = LocalFocusManager.current,
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    RoundLongButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "보내기",
                        onClick = {
                            if (content.isNotEmpty()) {
                                onSend(content)
                                onClose()
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.height(13.dp))
                Text(
                    text = "빈 화면을 터치하면 닫혀요!",
                    color = White,
                    style = Typography.headlineSmall,
                )
            }
        }
    }
}

@Preview
@Composable
fun LoveMentionPreview() {
    LoveMention(
        user = User(),
        onClose = {},
        onSend = {},
    )
}
