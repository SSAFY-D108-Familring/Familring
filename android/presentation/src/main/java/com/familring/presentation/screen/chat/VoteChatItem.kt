package com.familring.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.familring.presentation.R
import com.familring.presentation.theme.Blue02
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Red01
import com.familring.presentation.theme.Typography

@Composable
fun VoteChatItem(
    title: String,
    select: String,
) {
    Column(
        modifier = Modifier.wrapContentSize(),
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
            painter = painterResource(id = R.drawable.img_agree),
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

@Preview(showBackground = true)
@Composable
fun VoteChatItemPreview() {
    VoteChatItem(
        title = "오늘 저녁 치킨 어때유?",
        select = "찬성",
    )
}
