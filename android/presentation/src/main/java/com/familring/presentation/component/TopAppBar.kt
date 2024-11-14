package com.familring.presentation.component

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    tutorialIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (navigationType == TopAppBarNavigationType.Back) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.noRippleClickable { onNavigationClick() },
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "ic_back",
                tint = Black,
            )
            Spacer(modifier = Modifier.width(10.dp))
        } else {
            Spacer(modifier = Modifier.width(18.dp))
        }
        title()
        Spacer(modifier = Modifier.width(10.dp))
        tutorialIcon()
        Spacer(modifier = Modifier.weight(1f))
        trailingIcon()
        Spacer(modifier = Modifier.width(10.dp))
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
        navigationType = TopAppBarNavigationType.None,
        trailingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "ic_back",
                tint = Black,
            )
        },
    )
}
