package com.familring.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.familring.domain.Profile
import com.familring.presentation.R

@Composable
fun ZodiacBackgroundProfile(
    profile: Profile,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(profile.backgroundColor))),
    ) {
        Image(
            modifier =
                modifier
                    .size(55.dp)
                    .padding(8.dp)
                    .fillMaxSize(),
            painter = painterResource(id = R.drawable.img_wrapped_gift),
            contentDescription = "zodiac_profile",
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview
@Composable
private fun ZodiacBackgroundProfilePreview() {
    ZodiacBackgroundProfile(
        profile = Profile(zodiacImgUrl = "url1", backgroundColor = "#FEE222"),
    )
}
