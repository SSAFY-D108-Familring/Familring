package com.familring.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familring.presentation.R
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable

/**
 * CustomDropDownMenu
 * tabList에 메뉴 이름들을 List 형태로 넣어 주면 됨.
 * 사용 예시는 프리뷰에
 */

// style
data class IconCustomDropBoxStyles(
    val containerColor: Color = White,
    val cornerRadius: Dp = 10.dp,
    val textStyle: TextStyle = Typography.bodySmall.copy(fontSize = 18.sp, color = Black),
    val menuItemTextStyle: TextStyle = Typography.labelSmall.copy(fontSize = 18.sp, color = Black),
    val menuItemHeight: Dp = 30.dp,
    val iconSize: Dp = 24.dp,
    val iconColor: Color = Black,
    val iconDrawableId: Int = R.drawable.ic_more,
    val expandedIconDrawableId: Int = R.drawable.ic_more,
)

fun IconCustomDropBoxStyles.getIconDrawable(expanded: Boolean): Int = if (expanded) expandedIconDrawableId else iconDrawableId

@Composable
fun IconCustomDropdownMenu(
    modifier: Modifier = Modifier,
    menuItems: List<Pair<String, () -> Unit>>,
    styles: IconCustomDropBoxStyles = IconCustomDropBoxStyles(),
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        DropdownButton(
            iconDrawableId = styles.getIconDrawable(expanded),
            styles = styles,
        ) {
            expanded = true
        }
        if (expanded) {
            Box(modifier = Modifier.padding(3.dp)) {
                DropdownMenu(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .background(color = Color.Unspecified),
                    shape = RoundedCornerShape(styles.cornerRadius),
                    containerColor = Color.White,
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    menuItems.forEach { (label, onItemClick) ->
                        DropdownMenuItem(
                            modifier =
                                Modifier
                                    .wrapContentWidth()
                                    .height(styles.menuItemHeight),
                            text = {
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = label,
                                    style = styles.textStyle,
                                )
                            },
                            onClick = {
                                expanded = false
                                onItemClick()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownButton(
    modifier: Modifier = Modifier,
    iconDrawableId: Int?,
    styles: IconCustomDropBoxStyles,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        iconDrawableId?.let {
            Icon(
                modifier =
                    Modifier
                        .size(styles.iconSize),
                painter = painterResource(id = it),
                contentDescription = null,
                tint = styles.iconColor,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomDropdownMenuPreview() {
    IconCustomDropdownMenu(
        modifier = Modifier,
        menuItems =
            listOf(
                "수정" to {},
                "삭제" to {},
                "앨범 생성" to {},
            ),
        styles = IconCustomDropBoxStyles(),
    )
}
