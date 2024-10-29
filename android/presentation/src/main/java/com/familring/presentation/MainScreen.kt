package com.familring.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.familring.presentation.navigation.ScreenDestinations
import com.familring.presentation.screen.question.QuestionListScreen
import com.familring.presentation.screen.question.QuestionScreen
import com.familring.presentation.screen.signup.BirthRoute
import com.familring.presentation.screen.signup.DoneRoute
import com.familring.presentation.screen.signup.FamilyCountRoute
import com.familring.presentation.screen.signup.FirstRoute
import com.familring.presentation.screen.signup.NicknameRoute
import com.familring.presentation.screen.signup.PictureRoute
import com.familring.presentation.screen.signup.ProfileColorRoute
import com.familring.presentation.screen.timecapsule.NoTimeCapsuleScreen
import com.familring.presentation.screen.timecapsule.TimeCapsuleCreateScreen
import com.familring.presentation.screen.timecapsule.TimeCapsuleListScreen
import com.familring.presentation.screen.timecapsule.WritingTimeCapsuleScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackBarHostState = remember { SnackbarHostState() } // 스낵바 호스트
    val onShowSnackBar: (message: String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    // statusBar, navigationBar 색상 설정
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    systemUiController.setNavigationBarColor(color = Color.White)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { _ ->
        MainNavHost(
            modifier = modifier,
            navController = navController,
            startDestination = ScreenDestinations.First.route,
            showSnackBar = onShowSnackBar,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    showSnackBar: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = ScreenDestinations.First.route,
        ) {
            FirstRoute(
                modifier = modifier,
                navigateToBirth = {
                    navController.navigate(ScreenDestinations.Birth.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Birth.route,
        ) {
            BirthRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToColor = {
                    navController.navigate(ScreenDestinations.ProfileColor.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.ProfileColor.route,
        ) {
            ProfileColorRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToNickname = {
                    navController.navigate(ScreenDestinations.Nickname.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Nickname.route,
        ) {
            NicknameRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToPicture = {
                    navController.navigate(ScreenDestinations.Picture.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Picture.route,
        ) {
            PictureRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToCount = {
                    navController.navigate(ScreenDestinations.FamilyCount.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.FamilyCount.route,
        ) {
            FamilyCountRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToDone = {
                    navController.navigate(ScreenDestinations.Done.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Done.route,
        ) {
            DoneRoute(
                modifier = modifier,
            )
        }

        composable(
            route = ScreenDestinations.Question.route,
        ) {
            QuestionScreen(
                navigateToQuestionList = {
                    navController.navigate(ScreenDestinations.QuestionList.route)
                }
            )
        }

        composable(
            route = ScreenDestinations.QuestionList.route,
        ) {
            QuestionListScreen(
                onNavigateBack = navController::popBackStack
            )
        }

        composable(
            route = ScreenDestinations.TimeCapsuleList.route,
        ) {
            TimeCapsuleListScreen(
                modifier = modifier,
                onShowSnackBar = { },
                navigationToCapsule = { },
            )
        }

        composable(
            route = ScreenDestinations.NoTimeCapsule.route,
        ) {
            NoTimeCapsuleScreen(modifier = modifier)
        }

        composable(
            route = ScreenDestinations.TimeCapsuleCreate.route,
        ) {
            TimeCapsuleCreateScreen(modifier = modifier)
        }

        composable(
            route = ScreenDestinations.WritingTimeCapsule.route,
        ) {
            WritingTimeCapsuleScreen(
                modifier = modifier,
            )
        }
    }
}
