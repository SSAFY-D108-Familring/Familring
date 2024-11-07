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
                    navController.navigate(ScreenDestinations.Birth.route) {
                        popUpTo("First")
                    }
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Birth.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
                )

            BirthRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToColor = {
                    navController.navigate(ScreenDestinations.ProfileColor.route) {
                        popUpTo("First")
                    }
                },
                viewModel = viewModel,
            )
        }

        composable(
            route = ScreenDestinations.ProfileColor.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
                )

            ProfileColorRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                navigateToNickname = {
                    navController.navigate(ScreenDestinations.Nickname.route) {
                        popUpTo("First")
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Nickname.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
                )

            NicknameRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                navigateToPicture = {
                    navController.navigate(ScreenDestinations.Picture.route) {
                        popUpTo("First")
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Picture.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
                )

            PictureRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
                navigateToCount = {
                    navController.navigate(ScreenDestinations.FamilyInfo.route) {
                        popUpTo("First")
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.FamilyInfo.route,
        ) {
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
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
                        popUpTo("First")
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
                    navController.getBackStackEntry("First"),
                )

            DoneRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo("First")
                        launchSingleTop = true
                    }
                },
                showSnackBar = showSnackBar,
            )
        }
    }
}
