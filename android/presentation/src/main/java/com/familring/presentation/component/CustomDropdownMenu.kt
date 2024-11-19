package com.familring.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White

// style
data class CustomDropdownMenuStyles(
    val height: Dp = 45.dp,
    val mainColor: Color = White,
    val strokeColor: Color = Color.Transparent,
    val containerColor: Color = White,
    val cornerRadius: Dp = 10.dp,
    val verticalPadding: Dp = 8.dp,
    val horizontalPadding: Dp = 10.dp,
    val borderStroke: BorderStroke = BorderStroke(1.dp, strokeColor),
    val iconColor: Color = Black,
)

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    menuItems: List<Pair<String, () -> Unit>>,
    cornerRadius: Int = 0,
    iconDrawable: Int = 0,
    expandedIconDrawable: Int = 0,
    menuItemHeight: Int = 0,
    menuItemTextStyle: TextStyle = Typography.titleMedium,
    textStyle: TextStyle = Typography.titleMedium,
    styles: CustomDropdownMenuStyles = CustomDropdownMenuStyles(),
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedMenu = remember { mutableStateOf("선택해 주세요") }

    Column(modifier = modifier) {
        OutlinedDropdownButton(
            buttonText = selectedMenu.value,
            iconDrawableId = if (expanded.value) expandedIconDrawable else iconDrawable,
            styles = styles,
            textStyle =
                textStyle.copy(
                    color = if (selectedMenu.value == "선택해 주세요") Gray02 else Black,
                ),
        ) {
            expanded.value = true
        }
        Spacer(modifier = Modifier.height(4.dp))
        DropdownMenu(
            modifier =
                Modifier
                    .wrapContentWidth()
                    .background(color = Color.Unspecified),
            shape = RoundedCornerShape(cornerRadius.dp),
            containerColor = Color.White,
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            menuItems.forEach { (label, onClick) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            style = menuItemTextStyle,
                            color = Black,
                        )
                    },
                    onClick = {
                        expanded.value = false
                        selectedMenu.value = label
                        onClick()
                    },
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .height(menuItemHeight.dp),
                )
            }
        }
    }
}

@Composable
fun OutlinedDropdownButton(
    buttonText: String,
    iconDrawableId: Int?,
    styles: CustomDropdownMenuStyles,
    textStyle: TextStyle,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier =
            Modifier
                .height(styles.height)
                .wrapContentWidth(),
        border = styles.borderStroke,
        shape = RoundedCornerShape(styles.cornerRadius),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = styles.containerColor,
            ),
        contentPadding =
            PaddingValues(
                horizontal = styles.horizontalPadding,
                vertical = styles.verticalPadding,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = buttonText, style = textStyle)
            iconDrawableId?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = styles.iconColor,
                )
            }
        }
    }
}
