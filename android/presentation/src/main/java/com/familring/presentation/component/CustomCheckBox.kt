package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.White

@Composable
fun CustomCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChecked: (checked: Boolean) -> Unit = {},
) {
    if (isChecked) {
        CheckedCheckBox(
            modifier = modifier,
            onChecked = onChecked,
        )
    } else {
        UnCheckedCheckBox(
            modifier = modifier,
            onChecked = onChecked,
        )
    }
}

@Composable
fun UnCheckedCheckBox(
    modifier: Modifier = Modifier,
    onChecked: (checked: Boolean) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .size(20.dp)
                .background(
                    color = White,
                    shape = CircleShape,
                ).border(width = 1.dp, color = Gray02, shape = CircleShape)
                .clickable { onChecked(true) },
    ) {
        Icon(
            modifier = Modifier.padding(2.dp),
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = "ic_check",
            tint = Gray02,
        )
    }
}

@Composable
fun CheckedCheckBox(
    modifier: Modifier = Modifier,
    onChecked: (checked: Boolean) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .size(20.dp)
                .background(
                    color = Black,
                    shape = CircleShape,
                ).border(width = 1.dp, color = Black, shape = CircleShape)
                .clickable { onChecked(false) },
    ) {
        Icon(
            modifier = Modifier.padding(2.dp),
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = "ic_check",
            tint = White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckBoxPreview() {
    Column {
        CustomCheckBox(isChecked = false)
        CustomCheckBox(isChecked = true)
    }
}
