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
import com.familring.domain.Profile
import com.familring.domain.TimeCapsuleMessage
import com.familring.presentation.navigation.ScreenDestinations
import com.familring.presentation.screen.signup.BirthScreen
import com.familring.presentation.screen.signup.FirstScreen
import com.familring.presentation.screen.timecapsule.NoTimeCapsuleScreen
import com.familring.presentation.screen.timecapsule.TimeCapsuleCreateScreen
import com.familring.presentation.screen.timecapsule.TimeCapsuleDialog
import com.familring.presentation.screen.timecapsule.TimeCapsuleListScreen
import com.familring.presentation.screen.timecapsule.TimeCapsuleScreen
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
            startDestination = ScreenDestinations.TimeCapsuleDialog.route,
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
            FirstScreen(
                modifier = modifier,
                navigateToBirth = {
                    navController.navigate(ScreenDestinations.Birth.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Birth.route,
        ) {
            BirthScreen(modifier = modifier)
        }

        // 타임캡슐
        composable(
            route = ScreenDestinations.TimeCapsule.route,
        ) {
            TimeCapsuleScreen(
                modifier = modifier,
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
            NoTimeCapsuleScreen(
                modifier = modifier,
                navigateToCreate = { navController.navigate(ScreenDestinations.TimeCapsuleCreate.route) },
            )
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

        composable(
            route = ScreenDestinations.TimeCapsuleDialog.route,
        ) {
            TimeCapsuleDialog(
                modifier = modifier,
//                timeCapsuleMessages =
//                    listOf(
//                        TimeCapsuleMessage(
//                            id = 1,
//                            profile =
//                                Profile(
//                                    nickName = "엄마미",
//                                    zodiacImgUrl = "url",
//                                    backgroundColor = "#FEE222",
//                                ),
//                            message =
//                                "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
//                                    "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
//                                    "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
//                                    "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
//                        ),
//                        TimeCapsuleMessage(
//                            id = 1,
//                            profile =
//                                Profile(
//                                    nickName = "아빠미",
//                                    zodiacImgUrl = "url",
//                                    backgroundColor = "#FEA222",
//                                ),
//                            message =
//                                "이곳에는 이제 엄마의 타임캡슐이 적혀있을 것이오 " +
//                                    "뭐라고 적혀 있을진 모르겠지만 어쨌든 적혀 있음 " +
//                                    "더 길게 적어야 하나? 뭐... 잘 모르겠지만 " +
//                                    "이 다이얼로그의 길이는 적어놓을 것이오!!!!!!",
//                        ),
//                    ),
            )
        }
    }
}
