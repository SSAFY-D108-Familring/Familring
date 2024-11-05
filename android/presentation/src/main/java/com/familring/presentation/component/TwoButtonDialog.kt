package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.theme.Green02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun TwoButtonTextDialog(
    modifier: Modifier = Modifier,
    text: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .background(color = White, shape = RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = Typography.bodyMedium.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(horizontal = 24.dp, vertical = 36.dp),
            )
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(color = Green02),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.28f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onDismissClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "취소",
                        color = White,
                        style = Typography.headlineMedium.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                    )
                }
                VerticalDivider(
                    thickness = 1.dp,
                    color = White,
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onConfirmClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "확인",
                        color = White,
                        style = Typography.headlineMedium.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TwoButtonTextDialogPreview() {
    TwoButtonTextDialog(
        text = "작성된 캡슐이 없어요",
        onConfirmClick = {},
        onDismissClick = {},
    )
}
