package com.familring.presentation.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.Yellow01

@Composable
fun MyMessage(
    message: String,
    time: String,
    unReadMembers: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.End,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (unReadMembers != "0") {
                    Text(
                        text = unReadMembers,
                        style = Typography.titleSmall.copy(fontSize = 10.sp),
                        color = Green02,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
                Text(
                    text = time,
                    style = Typography.bodySmall.copy(fontSize = 12.sp),
                    color = Gray02,
                )
            }
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                modifier =
                    Modifier
                        .background(
                            color = Yellow01,
                            shape =
                                RoundedCornerShape(
                                    topStart = 15.dp,
                                    topEnd = 15.dp,
                                    bottomStart = 15.dp,
                                    bottomEnd = 0.dp,
                                ),
                        ).padding(horizontal = 13.dp, vertical = 10.dp),
                text = message,
                style = Typography.bodyLarge,
                color = Black,
            )
        }
    }
}
