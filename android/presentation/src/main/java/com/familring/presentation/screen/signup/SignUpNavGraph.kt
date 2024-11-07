package com.familring.presentation.screen.signup

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.familring.presentation.navigation.ScreenDestinations

fun NavGraphBuilder.SignUpNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
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
                    navController.getBackStackEntry(graphRoute),
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
                    navController.getBackStackEntry(graphRoute),
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
                    navController.getBackStackEntry(graphRoute),
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
                    navController.getBackStackEntry(graphRoute),
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
                    navController.getBackStackEntry(graphRoute),
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
                    navController.getBackStackEntry(graphRoute),
                )

            DoneRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route) {
                        launchSingleTop = true
                    }
                },
                showSnackBar = showSnackBar,
            )
        }
    }
}
