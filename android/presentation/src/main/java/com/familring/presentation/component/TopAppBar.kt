package com.familring.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable

enum class TopAppBarNavigationType { Back, None }

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationType: TopAppBarNavigationType = TopAppBarNavigationType.Back,
    onNavigationClick: () -> Unit = {},
    iconColor: Color = Black,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        if (navigationType == TopAppBarNavigationType.Back) {
            Icon(
                modifier = Modifier.noRippleClickable { onNavigationClick() },
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "ic_back",
                tint = Black,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        title()
    }
}

@Composable
@Preview(showBackground = true)
fun TopAppBarPreview() {
    TopAppBar(
        title = {
            Text(
                text = "프로필 배경색 설정",
                color = Black,
                style = Typography.headlineMedium.copy(fontSize = 22.sp),
            )
        },
    )
}
