package com.familring.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.familring.domain.model.calendar.DailyLife
import com.familring.domain.model.calendar.Schedule
import com.familring.presentation.navigation.BottomNavigationBar
import com.familring.presentation.navigation.ScreenDestinations
import com.familring.presentation.screen.calendar.CalendarRoute
import com.familring.presentation.screen.calendar.DailyUploadRoute
import com.familring.presentation.screen.calendar.ScheduleCreateRoute
import com.familring.presentation.screen.chat.ChatRoute
import com.familring.presentation.screen.gallery.AlbumRoute
import com.familring.presentation.screen.gallery.GalleryRoute
import com.familring.presentation.screen.gallery.GalleryViewModel
import com.familring.presentation.screen.gallery.PhotoRoute
import com.familring.presentation.screen.home.HomeRoute
import com.familring.presentation.screen.interest.InterestListRoute
import com.familring.presentation.screen.interest.InterestRoute
import com.familring.presentation.screen.interest.OtherInterestRoute
import com.familring.presentation.screen.login.LoginRoute
import com.familring.presentation.screen.mypage.EditColorRoute
import com.familring.presentation.screen.mypage.EditFaceRoute
import com.familring.presentation.screen.mypage.EditNameRoute
import com.familring.presentation.screen.mypage.MyPageRoute
import com.familring.presentation.screen.mypage.MyPageViewModel
import com.familring.presentation.screen.notification.NotificationRoute
import com.familring.presentation.screen.question.AnswerWriteRoute
import com.familring.presentation.screen.question.PastQuestionRoute
import com.familring.presentation.screen.question.QuestionListRoute
import com.familring.presentation.screen.question.QuestionRoute
import com.familring.presentation.screen.signup.SignUpNavGraph
import com.familring.presentation.screen.timecapsule.TimeCapsuleCreateRoute
import com.familring.presentation.screen.timecapsule.TimeCapsuleRoute
import com.familring.presentation.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val startDestination = remember(MainActivity.startDestination) {
        when (MainActivity.startDestination) {
            "question" -> ScreenDestinations.Question.route
            "schedule" -> ScreenDestinations.Calendar.route
            "chat" -> ScreenDestinations.Chat.route
            "timecapsule" -> ScreenDestinations.TimeCapsule.route
            "interest" -> ScreenDestinations.Interest.route
            else -> ScreenDestinations.Login.route
        }
    }

    val onShowSnackBar: (message: String) -> Unit = { message ->
        coroutineScope.launch {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val (visible, setVisible) = remember { mutableStateOf(false) }
    when (currentRoute) {
        "Home", "Question", "Calendar", "Gallery" -> setVisible(true)
        else -> setVisible(false)
    }

    // statusBar, navigationBar 색상 설정
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    systemUiController.setNavigationBarColor(color = White)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (visible) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        },
        containerColor = White,
    ) { innerPadding ->
        MainNavHost(
            modifier =
                modifier
                    .padding(innerPadding),
            navController = navController,
            startDestination = startDestination,
            showSnackBar = onShowSnackBar,
        )
    }

    LaunchedEffect(startDestination) {
        if (startDestination != ScreenDestinations.Login.route) {
            try {
                navController.navigate(startDestination) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                Timber.e(e, "Navigation failed")
            }
        }
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    showSnackBar: (String) -> Unit,
) {
    val galleryViewModel = hiltViewModel<GalleryViewModel>()

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.Login.route,
    ) {
        composable(
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                navigateToHome = {
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo(startDestination) {
                            inclusive = true
                        }
                    }
                },
                navigateToFirst = {
                    navController.navigate(ScreenDestinations.First.route)
                },
                showSnackBar = showSnackBar,
            )
        }

        SignUpNavGraph(
            modifier = modifier,
            navController = navController,
            showSnackBar = showSnackBar,
            graphRoute = "signup_graph",
        )

        composable(
            route = ScreenDestinations.Question.route,
        ) {
            QuestionRoute(
                modifier = modifier,
                navigateToQuestionList = {
                    navController.navigate(ScreenDestinations.QuestionList.route)
                },
                navigateToAnswerWrite = {
                    navController.navigate(ScreenDestinations.AnswerWrite.route)
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.AnswerWrite.route,
        ) {
            AnswerWriteRoute(
                modifier = modifier,
                onNavigateBack = navController::popBackStack,
                showSnackBar = showSnackBar,
            )
        }

        composable(route = ScreenDestinations.QuestionList.route) {
            QuestionListRoute(
                onNavigateBack = navController::popBackStack,
                navigateToPastQuestion = { questionId ->
                    navController.navigate(ScreenDestinations.PastQuestion.createRoute(questionId))
                },
            )
        }

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
                showSnackbar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Home.route,
        ) {
            HomeRoute(
                modifier = modifier,
                navigateToNotification = {
                    navController.navigate(ScreenDestinations.Notification.route) {
                        popUpTo(ScreenDestinations.Home.route)
                    }
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
            ChatRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Calendar.route,
        ) {
            CalendarRoute(
                modifier = modifier,
                navigateToCreateSchedule = {
                    navController.navigate(
                        ScreenDestinations.ScheduleCreate.createRoute(Schedule()),
                    )
                },
                navigateToModifySchedule = { schedule ->
                    navController.navigate(
                        ScreenDestinations.ScheduleCreate.createRoute(schedule, true),
                    )
                },
                navigateToCreateDaily = {
                    navController.navigate(ScreenDestinations.DailyUpload.createRoute(DailyLife()))
                },
                navigateToModifyDaily = { dailyLife ->
                    navController.navigate(
                        ScreenDestinations.DailyUpload.createRoute(
                            dailyLife,
                            true,
                        ),
                    )
                },
                navigateToCreateAlbum = { navController.navigate(ScreenDestinations.Gallery.route) },
                navigateToAlbum = { albumId, isNormal ->
                    navController.navigate(ScreenDestinations.Album.createRoute(albumId, isNormal))
                },
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.ScheduleCreate.route,
            arguments = ScreenDestinations.ScheduleCreate.arguments,
        ) { backStackEntry ->
            val schedule = backStackEntry.arguments?.getParcelable<Schedule>("targetSchedule")
            val isModify = backStackEntry.arguments?.getBoolean("isModify") ?: false

            if (schedule != null) {
                ScheduleCreateRoute(
                    modifier = modifier,
                    targetSchedule = schedule,
                    isModify = isModify,
                    popUpBackStack = navController::popBackStack,
                    showSnackBar = showSnackBar,
                )
            }
        }

        composable(
            route = ScreenDestinations.DailyUpload.route,
            arguments = ScreenDestinations.DailyUpload.argument,
        ) { backStackEntry ->
            val daily = backStackEntry.arguments?.getParcelable<DailyLife>("targetDaily")
            val isModify = backStackEntry.arguments?.getBoolean("isModify") ?: false

            if (daily != null) {
                DailyUploadRoute(
                    modifier = modifier,
                    targetDaily = daily,
                    isModify = isModify,
                    popUpBackStack = navController::popBackStack,
                    showSnackbar = showSnackBar,
                )
            }
        }

        composable(
            route = ScreenDestinations.Gallery.route,
        ) {
            GalleryRoute(
                modifier = modifier,
                navigateToAlbum = { albumId, isNormal ->
                    navController.navigate(ScreenDestinations.Album.createRoute(albumId, isNormal))
                },
                showSnackBar = showSnackBar,
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
                onNavigateBack = navController::popBackStack,
                onShowSnackBar = showSnackBar,
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
                onShowSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Notification.route,
        ) {
            NotificationRoute(
                modifier = modifier,
                navigateToHome =
                    navController::popBackStack,
                navigateToQuestion = {
                    navController.navigate(ScreenDestinations.Question.route) {
                        popUpTo(ScreenDestinations.Home.route)
                    }
                },
                navigateToSchedule = {
                    navController.navigate(ScreenDestinations.Calendar.route) {
                        popUpTo(ScreenDestinations.Home.route)
                    }
                },
                navigateToChat = {
                    navController.navigate(ScreenDestinations.Chat.route) {
                        popUpTo(ScreenDestinations.Home.route)
                    }
                },
                navigateToTimeCapsule = {
                    navController.navigate(ScreenDestinations.TimeCapsule.route) {
                        popUpTo(ScreenDestinations.Home.route)
                    }
                },
                navigateToInterest = {
                    navController.navigate(ScreenDestinations.Interest.route) {
                        popUpTo(
                            ScreenDestinations.Home.route,
                        )
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Album.route,
            arguments = ScreenDestinations.Album.arguments,
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0L
            val isNormal = backStackEntry.arguments?.getBoolean("isNormal") ?: false

            AlbumRoute(
                albumId = albumId,
                isNormal = isNormal,
                modifier = modifier,
                onNavigateBack = navController::popBackStack,
                onPhotoClick = { albumId, photoUrl ->
                    navController.navigate(ScreenDestinations.Photo.createRoute(albumId, photoUrl))
                },
                viewModel = galleryViewModel,
            )
        }

        composable(
            route = ScreenDestinations.Photo.route,
            arguments = ScreenDestinations.Photo.arguments,
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0L
            val photoUrl = backStackEntry.arguments?.getString("photoUrl") ?: ""
            PhotoRoute(
                albumId = albumId,
                photoUrl = photoUrl,
                modifier = modifier,
                onNavigateBack = navController::popBackStack,
                viewModel = galleryViewModel,
                showSnackbar = showSnackBar
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
                    navController.navigate(ScreenDestinations.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                navigateToEditName = {
                    navController.navigate(ScreenDestinations.EditName.route)
                },
                navigateToEditColor = {
                    navController.navigate(ScreenDestinations.EditColor.route)
                },
                navigateToEditFace = {
                    navController.navigate(ScreenDestinations.EditFace.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.EditName.route,
        ) {
            val viewModel =
                hiltViewModel<MyPageViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.MyPage.route),
                )

            EditNameRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.EditColor.route,
        ) {
            val viewModel =
                hiltViewModel<MyPageViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.MyPage.route),
                )
            EditColorRoute(
                modifier = modifier,
                viewModel = viewModel,
                popUpBackStack = navController::popBackStack,
                showSnackBar = showSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.PastQuestion.route,
            arguments = ScreenDestinations.PastQuestion.arguments,
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getLong("questionId") ?: 0L
            PastQuestionRoute(
                questionId = questionId,
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.EditFace.route,
        ) {
            val viewModel =
                hiltViewModel<MyPageViewModel>(
                    navController.getBackStackEntry(ScreenDestinations.MyPage.route),
                )
            EditFaceRoute(
                modifier = modifier,
                viewModel = viewModel,
                showSnackBar = showSnackBar,
                popUpBackStack = navController::popBackStack,
            )
        }
    }
    LaunchedEffect(startDestination) {
        if (startDestination != ScreenDestinations.Login.route) {
            navController.navigate(ScreenDestinations.Home.route) {
                popUpTo(0) { inclusive = true }
            }
            navController.navigate(startDestination!!) {
                launchSingleTop = true
                popUpTo(ScreenDestinations.Home.route)
            }
            MainActivity.startDestination = null
        }
    }
}
