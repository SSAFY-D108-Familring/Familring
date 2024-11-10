package com.familring.presentation.screen.interest

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.component.TopAppBar
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

@Composable
fun InterestRoute(
    modifier: Modifier,
    navigateToInterestList: () -> Unit,
    navigateToOtherInterest: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    InterestScreen(
        modifier = modifier,
        isWrote = true,
        interest = "엘지 트윈스",
        isShareDay = false,
        isShare = true,
        navigateToInterestList = navigateToInterestList,
        navigateToOtherInterest = navigateToOtherInterest,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun InterestScreen(
    modifier: Modifier = Modifier,
    navigateToInterestList: () -> Unit = {},
    navigateToOtherInterest: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    interest: String = "",
    isWrote: Boolean = false,
    isShare: Boolean = false,
    isShareDay: Boolean = false, // true: 인증샷 남기는 날, false: 작성하는 날
    writeInterest: (String) -> Unit = {},
    editInterest: (String) -> Unit = {},
    shareImage: (Uri) -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.interest_top_title),
                        color = Black,
                        style = Typography.titleLarge.copy(fontSize = 30.sp),
                    )
                },
                onNavigationClick = onNavigateBack,
                trailingIcon = {
                    Icon(
                        modifier =
                            Modifier.noRippleClickable {
                                navigateToInterestList()
                            },
                        painter = painterResource(id = R.drawable.img_menu),
                        contentDescription = "img_menu",
                    )
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (isShareDay) {
                ShareDayScreen(
                    isUpload = isShare,
                    shareImage = shareImage,
                    navigateToOtherInterest = navigateToOtherInterest,
                )
            } else {
                WriteDayScreen(
                    modifier = Modifier.imePadding(),
                    isWrote = isWrote,
                    interest = interest,
                    writeInterest = writeInterest,
                    editInterest = editInterest,
                    navigateToOtherInterest = navigateToOtherInterest,
                )
            }
        }
    }
}

@Preview
@Composable
fun InterestScreenPreview() {
    InterestScreen(
        interest = "엘지 트윈스",
    )
}
