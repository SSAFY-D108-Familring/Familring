package com.familring.presentation.component.tutorial

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Blue01
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Pink01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.theme.Yellow01
import com.familring.presentation.util.noRippleClickable

@Composable
fun TreeExplanation(onClose: () -> Unit = {}) {
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
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "소통나무 키우기  \uD83C\uDF31",
                        color = Green02,
                        style = Typography.titleSmall.copy(fontSize = 22.sp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "가족들의 소통 상태를 나무로 확인할 수 있어요.",
                        color = Gray01,
                        style = Typography.bodyMedium.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.padding(start = 13.dp),
                        text = "점수를 높여 소통 나무를 키워보세요!  \uD83D\uDE4C\uD83C\uDFFB",
                        color = Gray01,
                        style = Typography.bodyMedium.copy(fontSize = 15.sp),
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(color = Green01.copy(alpha = 0.25f)),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .wrapContentSize()
                                    .padding(horizontal = 19.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(13.dp),
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "레벨 1",
                                    color = Gray01,
                                    style = Typography.bodySmall.copy(fontSize = 12.sp),
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Image(
                                    painter =
                                        painterResource(id = R.drawable.img_tree_status_one),
                                    contentDescription = "level_one",
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "민둥맨둥",
                                    color = Green02,
                                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                                )
                            }
                            Column(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "레벨 2",
                                    color = Gray01,
                                    style = Typography.bodySmall.copy(fontSize = 12.sp),
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Image(
                                    painter =
                                        painterResource(id = R.drawable.img_tree_status_two),
                                    contentDescription = "level_one",
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "무럭무럭",
                                    color = Green02,
                                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                                )
                            }
                            Column(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "레벨 3",
                                    color = Gray01,
                                    style = Typography.bodySmall.copy(fontSize = 12.sp),
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Image(
                                    painter =
                                        painterResource(id = R.drawable.img_tree_status_three),
                                    contentDescription = "level_one",
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "파릇파릇",
                                    color = Green02,
                                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                                )
                            }
                            Column(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "레벨 4",
                                    color = Gray01,
                                    style = Typography.bodySmall.copy(fontSize = 12.sp),
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Image(
                                    painter =
                                        painterResource(id = R.drawable.img_tree_status_four),
                                    contentDescription = "level_one",
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "초록초록",
                                    color = Green02,
                                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .background(color = Pink01, shape = RoundedCornerShape(30.dp))
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "\uD83D\uDC8C  랜덤 질문",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 15.sp),
                        )
                        Spacer(modifier = Modifier.width(22.dp))
                        Text(
                            text = "모든 가족 참여 시 + 10",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 13.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .background(color = Blue01, shape = RoundedCornerShape(30.dp))
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "\uD83D\uDC8A  타임 캡슐",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 15.sp),
                        )
                        Spacer(modifier = Modifier.width(22.dp))
                        Text(
                            text = "가족 구성원 중 1명 참여 시 + 3",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 13.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .background(color = Yellow01, shape = RoundedCornerShape(30.dp))
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "\uD83C\uDF81  관심사 공유",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 15.sp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "가족 구성원 중 1명 관심사 인증 시 + 3",
                            color = Black,
                            style = Typography.headlineMedium.copy(fontSize = 13.sp),
                        )
                    }
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
fun TreeExplanationPreview() {
    TreeExplanation()
}
