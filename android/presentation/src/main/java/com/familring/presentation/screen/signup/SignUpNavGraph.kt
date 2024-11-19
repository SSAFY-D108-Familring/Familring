package com.familring.presentation.screen.signup

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.familring.presentation.navigation.ScreenDestinations

fun NavGraphBuilder.SignUpNavGraph(
    modifier: Modifier,
    navController: NavController,
    showSnackBar: (String) -> Unit,
    graphRoute: String,
) {
    navigation(startDestination = ScreenDestinations.First.route, route = graphRoute) {
        composable(
            route = ScreenDestinations.First.route,
        ) {
            FirstRoute(
                modifier = modifier,
                navigateToBirth = {
                    navController.navigate(ScreenDestinations.Birth.route)
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Birth.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            BirthRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToColor = {
                    navController.navigate(ScreenDestinations.ProfileColor.route)
                },
                viewModel = viewModel,
            )
        }

        composable(
            route = ScreenDestinations.ProfileColor.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            ProfileColorRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                navigateToNickname = {
                    navController.navigate(ScreenDestinations.Nickname.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Nickname.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            NicknameRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                navigateToPicture = {
                    navController.navigate(ScreenDestinations.Picture.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Picture.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            PictureRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
                navigateToCount = {
                    navController.navigate(ScreenDestinations.FamilyInfo.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.FamilyInfo.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            FamilyInfoRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
                navigateToDone = {
                    navController.navigate(ScreenDestinations.Done.route)
                },
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.First.route)
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Done.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.First.route),
                )

            DoneRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.First.route)
                        launchSingleTop = true
                    }
                },
                showSnackBar = showSnackBar,
            )
        }
    }
}
