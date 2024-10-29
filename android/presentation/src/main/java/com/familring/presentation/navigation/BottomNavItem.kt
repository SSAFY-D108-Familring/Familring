package com.familring.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.familring.presentation.R

sealed class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: String,
) {
    data object Home : BottomNavItem(
        title = R.string.home_title,
        icon = R.drawable.ic_home,
        selectedIcon = R.drawable.ic_fill_home,
        route = ScreenDestinations.Home.route,
    )

    data object Chat : BottomNavItem(
        title = R.string.chat_title,
        icon = R.drawable.ic_chat,
        selectedIcon = R.drawable.ic_fill_chat,
        route = ScreenDestinations.Chat.route,
    )

    data object Question : BottomNavItem(
        title = R.string.question_title,
        icon = R.drawable.ic_question,
        selectedIcon = R.drawable.ic_fill_question,
        route = ScreenDestinations.Home.route, // 임시
    )

    data object Calendar : BottomNavItem(
        title = R.string.calendar_title,
        icon = R.drawable.ic_calendar,
        selectedIcon = R.drawable.ic_fill_calendar,
        route = ScreenDestinations.Home.route, // 임시
    )

    data object Album : BottomNavItem(
        title = R.string.album_title,
        icon = R.drawable.ic_gallery,
        selectedIcon = R.drawable.ic_fill_gallery,
        route = ScreenDestinations.Home.route, // 임시
    )
}
