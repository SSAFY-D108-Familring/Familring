package com.familring.presentation.navigation

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Green01
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
) {
    val items =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Chat,
            BottomNavItem.Question,
            BottomNavItem.Calendar,
            BottomNavItem.Gallery,
        )

    BottomAppBar(
        modifier =
            modifier
                .fillMaxHeight(0.075f)
                .shadow(elevation = 20.dp),
        containerColor = White,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val icon = if (isSelected) item.selectedIcon else item.icon

            NavigationBarItem(
                interactionSource = NoRippleInteractionSource,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = Green01,
                        unselectedIconColor = Gray02,
                        selectedTextColor = Green01,
                        unselectedTextColor = Gray02,
                        indicatorColor = Color.Transparent,
                    ),
                selected = isSelected,
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        style =
                            if (isSelected) {
                                Typography.headlineSmall.copy(fontSize = 12.sp)
                            } else {
                                Typography.bodySmall.copy(fontSize = 12.sp)
                            },
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = stringResource(id = item.title),
                        tint = Color.Unspecified,
                    )
                },
            )
        }
    }
}

/**
 * 클릭시 알약 파장 효과 제거
 */
private object NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}
