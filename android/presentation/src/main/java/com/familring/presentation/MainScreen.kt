package com.familring.presentation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.familring.presentation.navigation.BottomNavigationBar
import com.familring.presentation.navigation.ScreenDestinations
import com.familring.presentation.screen.calendar.CalendarRoute
import com.familring.presentation.screen.calendar.DailyUploadRoute
import com.familring.presentation.screen.calendar.ScheduleCreateRoute
import com.familring.presentation.screen.chat.ChatRoute
import com.familring.presentation.screen.gallery.AlbumRoute
import com.familring.presentation.screen.gallery.GalleryRoute
import com.familring.presentation.screen.home.HomeRoute
import com.familring.presentation.screen.interest.InterestListRoute
import com.familring.presentation.screen.interest.InterestRoute
import com.familring.presentation.screen.interest.OtherInterestRoute
import com.familring.presentation.screen.login.LoginRoute
import com.familring.presentation.screen.mypage.MyPageRoute
import com.familring.presentation.screen.notification.NotificationRoute
import com.familring.presentation.screen.question.QuestionListScreen
import com.familring.presentation.screen.question.QuestionScreen
import com.familring.presentation.screen.signup.BirthRoute
import com.familring.presentation.screen.signup.DoneRoute
import com.familring.presentation.screen.signup.FamilyInfoRoute
import com.familring.presentation.screen.signup.FirstRoute
import com.familring.presentation.screen.signup.NicknameRoute
import com.familring.presentation.screen.signup.PictureRoute
import com.familring.presentation.screen.signup.ProfileColorRoute
import com.familring.presentation.screen.signup.SignUpViewModel
import com.familring.presentation.screen.timecapsule.TimeCapsuleCreateRoute
import com.familring.presentation.screen.timecapsule.TimeCapsuleRoute
import com.familring.presentation.theme.White
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

    val (visible, setVisible) = remember { mutableStateOf(false) }
    when (currentRoute) {
        "Home", "Chat", "Question", "Calendar", "Gallery" -> setVisible(true)
        else -> setVisible(false)
    }

    // statusBar, navigationBar 색상 설정
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    systemUiController.setNavigationBarColor(color = White)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (visible) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        },
    ) { innerPadding ->
        MainNavHost(
            modifier =
                modifier
                    .padding(innerPadding)
                    .navigationBarsPadding(),
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
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
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route)
                },
                navigateToFirst = {
                    navController.navigate(ScreenDestinations.First.route)
                },
                showSnackBar = showSnackBar,
            )
        }

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
            val viewModel =
                hiltViewModel<SignUpViewModel>(
                    navController.getBackStackEntry("First"),
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
                    navController.getBackStackEntry("First"),
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
                    navController.getBackStackEntry("First"),
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
                    navController.getBackStackEntry("First"),
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
                    navController.navigate(ScreenDestinations.Home.route)
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
                    navController.navigate(ScreenDestinations.Home.route)
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Question.route,
        ) {
            QuestionScreen(
                navigateToQuestionList = {
                    navController.navigate(ScreenDestinations.QuestionList.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.QuestionList.route,
        ) {
            QuestionListScreen(
                onNavigateBack = navController::popBackStack,
            )
        }

        // 타임캡슐
        composable(
            route = ScreenDestinations.TimeCapsule.route,
        ) {
            TimeCapsuleRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToCreate = {
                    navController.navigate(ScreenDestinations.TimeCapsuleCreate.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.TimeCapsuleCreate.route,
        ) {
            TimeCapsuleCreateRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Home.route,
        ) {
            HomeRoute(
                modifier = modifier,
                navigateToNotification = {
                    navController.navigate(ScreenDestinations.Notification.route)
                },
                navigateToTimeCapsule = {
                    navController.navigate(ScreenDestinations.TimeCapsule.route)
                },
                navigateToInterest = {
                    navController.navigate(ScreenDestinations.Interest.route)
                },
                navigateToMyPage = {
                    navController.navigate(ScreenDestinations.MyPage.route)
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Chat.route,
        ) {
            ChatRoute(modifier = modifier)
        }

        composable(
            route = ScreenDestinations.Calendar.route,
        ) {
            CalendarRoute(
                modifier = modifier,
                navigateToCreateSchedule = { navController.navigate(ScreenDestinations.ScheduleCreate.route) },
                navigateToCreateDaily = { navController.navigate(ScreenDestinations.DailyUpload.route) },
                navigateToCreateAlbum = { navController.navigate(ScreenDestinations.Gallery.route) },
                navigateToAlbum = {},
                navigateToModifySchedule = { },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.ScheduleCreate.route,
        ) {
            ScheduleCreateRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.DailyUpload.route,
        ) {
            DailyUploadRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Gallery.route,
        ) {
            GalleryRoute(
                modifier = modifier,
                navigateToAlbum = {
                    navController.navigate(ScreenDestinations.Album.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Interest.route,
        ) {
            InterestRoute(
                modifier = modifier,
                navigateToInterestList = {
                    navController.navigate(ScreenDestinations.InterestList.route)
                },
                navigateToOtherInterest = {
                    navController.navigate(ScreenDestinations.OtherInterest.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.InterestList.route,
        ) {
            InterestListRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.OtherInterest.route,
        ) {
            OtherInterestRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Notification.route,
        ) {
            NotificationRoute(
                modifier = modifier,
                navigateToHome =
                    navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Album.route,
        ) {
            AlbumRoute(
                modifier = modifier,
                onNavigateBack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.MyPage.route,
        ) {
            MyPageRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
                navigateToLogin = {
                    navController.navigate(ScreenDestinations.Login.route)
                },
            )
        }
    }
}
