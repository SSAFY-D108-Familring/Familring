package com.familring.presentation.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
fun OneButtonTextDialog(
    modifier: Modifier = Modifier,
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit,
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
                    .background(color = Green02)
                    .clickable { onButtonClick() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = buttonText,
                modifier =
                    Modifier
                        .padding(vertical = 16.dp),
                color = White,
                style = Typography.headlineMedium.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun OneButtonTextDialogPreview() {
    OneButtonTextDialog(text = "작성된 캡슐이 없어요", buttonText = "확인", onButtonClick = {})
}
