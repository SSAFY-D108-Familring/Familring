package com.familring.presentation.component.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.theme.Blue02
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.theme.Yellow01

@Composable
fun VoteResultMessage(
    title: String,
    result: String,
    agreeList: List<Profile>,
    disagreeList: List<Profile>,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth(0.75f)
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
                        text = "투표가 종료됐어요",
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
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        text = title,
                        style = Typography.displaySmall.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        style = Typography.titleLarge.copy(fontSize = 26.sp),
                        color = if (result == "찬성") Blue02 else Red01,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "찬성",
                            style = Typography.bodySmall,
                            color = Gray01,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        OverlappingProfileLazyRow(
                            profiles = agreeList,
                            profileSize = 25,
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${agreeList.size}명",
                            style = Typography.bodySmall,
                            color = Gray01,
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "반대",
                            style = Typography.bodySmall,
                            color = Gray01,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        OverlappingProfileLazyRow(
                            profiles = disagreeList,
                            profileSize = 25,
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${disagreeList.size}명",
                            style = Typography.bodySmall,
                            color = Gray01,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteResultMessagePreview() {
    VoteResultMessage(
        title = "오늘 저녁 치킨 어때유?",
        result = "찬성",
        agreeList =
            listOf(
                Profile(
                    nickname = "나갱",
                    zodiacImgUrl = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/닭.png",
                    backgroundColor = "0xFFFFE1E1",
                ),
                Profile(
                    nickname = "ㅇㅇ",
                    zodiacImgUrl = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/양.png",
                    backgroundColor = "0xFFC9D0FF",
                ),
            ),
        disagreeList =
            listOf(
                Profile(
                    nickname = "ㅁㄴㅇㄹ",
                    zodiacImgUrl = "https://familring-bucket.s3.ap-northeast-2.amazonaws.com/zodiac-sign/소.png",
                    backgroundColor = "0xFF949494",
                ),
            ),
    )
}
