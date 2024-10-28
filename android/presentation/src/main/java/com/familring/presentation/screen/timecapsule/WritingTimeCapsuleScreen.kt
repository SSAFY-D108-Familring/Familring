package com.familring.presentation.screen.timecapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.domain.Profile
import com.familring.presentation.R
import com.familring.presentation.component.GreenRoundLongButton
import com.familring.presentation.component.OverlappingProfileLazyRow
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Gray04
import com.familring.presentation.theme.Typography

@Composable
fun WritingTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    letterCount: Int = 0,
    wroteProfiles: List<Profile> = listOf(),
) {
    var content by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(content) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.img_pill),
                contentDescription = "pill",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = "${letterCount}번째 타임캡슐을 작성해 보세요!",
                style = Typography.headlineLarge.copy(fontSize = 26.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Text(
                text = "미래 속 가족에게 하고 싶은 말이 있나요?",
                style =
                    Typography.bodySmall.copy(
                        color = Gray01,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.5f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Gray04)
                        .padding(16.dp),
            ) {
                BasicTextField(
                    value = content,
                    onValueChange = { newValue ->
                        content = newValue
                    },
                    modifier =
                        Modifier.verticalScroll(
                            state = scrollState,
                        ),
                    textStyle =
                        Typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            color = Black,
                        ),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "여기에 작성해주세요",
                                style =
                                    Typography.bodyMedium.copy(
                                        fontSize = 20.sp,
                                        color = Gray03,
                                    ),
                            )
                        }
                        innerTextField()
                    },
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            GreenRoundLongButton(
                text = "작성 완료",
                onClick = { /*TODO*/ },
                enabled = content.isNotEmpty(),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.06f))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f),
                text = "작성 완료한 가족",
                style =
                    Typography.displaySmall.copy(
                        fontSize = 18.sp,
                        color = Gray01,
                    ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            OverlappingProfileLazyRow(
                modifier = Modifier.fillMaxWidth(0.9f),
                profiles = wroteProfiles,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
        }
    }
}

@Preview
@Composable
private fun WritingTimeCapsuleScreenPreview() {
    WritingTimeCapsuleScreen(
        letterCount = 2,
        wroteProfiles =
            listOf(
                Profile("url1", "#FEE222"),
                Profile("url1", "#FFE1E1"),
                Profile("url1", "#FEE222"),
            ),
    )
}
