package com.familring.presentation.screen.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.util.noRippleClickable
import java.time.LocalDate

@Composable
fun MonthController(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    date: LocalDate,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "${date.monthValue}월",
            style = Typography.titleLarge.copy(fontSize = 22.sp),
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = date.year.toString(),
            style = Typography.labelMedium.copy(fontSize = 14.sp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier =
                Modifier.noRippleClickable { onPrevClick() },
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "ic_prev",
            tint = Black,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Icon(
            modifier =
                Modifier
                    .noRippleClickable { onNextClick() }
                    .rotate(180f),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "ic_back",
            tint = Black,
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthPreview() {
    val today = LocalDate.now()
    MonthController(
        date = today,
    )
}
