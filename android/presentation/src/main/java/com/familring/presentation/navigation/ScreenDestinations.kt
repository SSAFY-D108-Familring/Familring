package com.familring.presentation.navigation

/*
 추가 하는 방법
    data object sample : ScreenDestination(route = "화면이름"){
        // 넘기는 인자 있어야 하는 경우
        val arguments = listOf(
            navArgument(name = "id") { type = NavType.StringType },
            navArgument(name = "number") { type = NavType.IntType },
        )
        fun createRoute(
            id : String,
            number : Int
        ) = this.route/id/$number
    }
 */

sealed class ScreenDestinations(
    open val route: String,
) {
    // 첫 번째 화면 (초대코드 입력)
    data object First : ScreenDestinations(route = "First")

    // 생년월일 입력
    data object Birth : ScreenDestinations(route = "Birth")

    // 프로필 색상 선택
    data object ProfileColor : ScreenDestinations(route = "ProfileColor")

    // 닉네임
    data object Nickname : ScreenDestinations(route = "Nickname")

    // 사진 촬영
    data object Picture : ScreenDestinations(route = "Picture")

    // 구성원 수 입력
    data object FamilyCount : ScreenDestinations(route = "FamilyCount")

    // 회원가입 완료
    data object Done : ScreenDestinations(route = "Done")
}
