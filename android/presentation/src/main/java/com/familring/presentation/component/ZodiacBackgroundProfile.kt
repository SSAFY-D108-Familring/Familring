package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.familring.domain.model.Profile
import com.familring.presentation.util.toColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ZodiacBackgroundProfile(
    profile: Profile,
    modifier: Modifier = Modifier,
    size: Int = 55,
    paddingValue: Int = 8,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(profile.backgroundColor.toColor()),
    ) {
        GlideImage(
            modifier =
                Modifier
                    .size(size.dp)
                    .padding(paddingValue.dp)
                    .fillMaxSize(),
            model = profile.zodiacImgUrl,
            contentDescription = "zodiac_profile",
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview
@Composable
private fun ZodiacBackgroundProfilePreview() {
    ZodiacBackgroundProfile(
        profile =
            Profile(
                zodiacImgUrl = "url1",
                backgroundColor = "0xFFFEE222",
            ),
    )
}
