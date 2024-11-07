package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.model.timecapsule.TimeCapsule
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

@Composable
fun TimeCapsuleListScreen(
    modifier: Modifier = Modifier,
    state: TimeCapsuleUiState,
    getTimeCapsules: () -> Unit = {},
    onShowSnackBar: (message: String) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        getTimeCapsules()
    }

    if (state.timeCapsules.isEmpty()) {
        NoTimeCapsuleList(modifier = modifier)
    } else {
        TimeCapsuleList(
            modifier = modifier,
            timeCapsules = state.timeCapsules,
            onShowSnackBar = onShowSnackBar,
        )
    }
}

@Composable
fun TimeCapsuleList(
    modifier: Modifier = Modifier,
    timeCapsules: List<TimeCapsule> = listOf(),
    onShowSnackBar: (message: String) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(0.9f),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(timeCapsules.reversed()) { index, item ->
                    TimeCapsuleItem(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                        count = timeCapsules.size - index,
                        timeCapsule = item,
                        onShowSnackBar = onShowSnackBar,
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }
}

@Composable
fun TimeCapsuleItem(
    modifier: Modifier = Modifier,
    count: Int,
    timeCapsule: TimeCapsule,
    onShowSnackBar: (message: String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Gray03,
                    shape = RoundedCornerShape(12.dp),
                ).clickable {
                    if (timeCapsule.leftDays > 0) {
                        onShowSnackBar("아직 캡슐을 열 수 없어요!")
                    } else {
                        showDialog = true
                    }
                },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
        ) {
            Text(
                modifier = Modifier.padding(start = 3.dp),
                text = "${count}번째 캡슐",
                style =
                    Typography.headlineLarge.copy(
                        color = Black,
                        fontSize = 17.sp,
                    ),
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Image(
                    modifier =
                        Modifier
                            .width(65.dp),
                    painter = painterResource(id = R.drawable.img_wrapped_gift),
                    contentDescription = "wrapped_gift",
                )
            }
        }

        if (timeCapsule.leftDays > 0) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = Gray01.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "D - ${timeCapsule.leftDays}",
                    style =
                        Typography.titleLarge.copy(
                            fontSize = 20.sp,
                            color = White,
                        ),
                )
            }
        }
    }

    if (showDialog) {
        TimeCapsuleDialog(
            onDismiss = { showDialog = false },
            timeCapsuleMessages = timeCapsule.messages,
        )
    }
}

// @Preview(showBackground = true)
// @Composable
// private fun TimeCapsuleItemPreview() {
//    TimeCapsuleItem(
//        count = 2,
//        openable = true,
//    )
// }

@Preview
@Composable
private fun TimeCapsuleListPreview() {
    TimeCapsuleListScreen(
        state = TimeCapsuleUiState(),
    )
}
