package com.familring.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.familring.domain.model.calendar.Schedule
import com.familring.presentation.LocalDateTimeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk.type
import java.time.LocalDateTime

/*
 받는 변수가 있는 스크린 추가
    data object sample : ScreenDestination(route = "화면이름"){
        override val route: String
            get() = "화면이름/인자변수명"
        val arguments = listOf(
            navArgument(name = "id") { type = NavType.StringType },
        )
        fun createRoute(
            id : String,
        ) = "화면이름/$id"
    }
 */

sealed class ScreenDestinations(
    open val route: String,
) {
    val gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

    // 첫 번째 화면 (초대코드 입력)
    data object First : ScreenDestinations(route = "First")

    // 생년월일 입력
    data object Birth : ScreenDestinations(route = "Birth")

    // 로그인
    data object Login : ScreenDestinations(route = "Login")

    // 질문 화면
    data object Question : ScreenDestinations(route = "Question")

    // 질문 목록 화면
    data object QuestionList : ScreenDestinations(route = "QuestionList")

    // 프로필 색상 선택
    data object ProfileColor : ScreenDestinations(route = "ProfileColor")

    // 닉네임
    data object Nickname : ScreenDestinations(route = "Nickname")

    // 사진 촬영
    data object Picture : ScreenDestinations(route = "Picture")

    // 구성원 수 입력
    data object FamilyInfo : ScreenDestinations(route = "FamilyCount")

    // 회원가입 완료
    data object Done : ScreenDestinations(route = "Done")

    // 타임캡슐
    data object TimeCapsule : ScreenDestinations(route = "TimeCapsule")

    // 타임캡슐 생성 화면
    data object TimeCapsuleCreate : ScreenDestinations(route = "TimeCapsuleCreate")

    // 홈
    data object Home : ScreenDestinations(route = "Home")

    // 채팅
    data object Chat : ScreenDestinations(route = "Chat")

    // 캘린더
    data object Calendar : ScreenDestinations(route = "Calendar")

    // 일정 생성
    data object ScheduleCreate : ScreenDestinations(route = "ScheduleCreate") {
        override val route: String
            get() = "ScheduleCreate/{targetSchedule}/{isModify}"

        val arguments =
            listOf(
                navArgument(name = "targetSchedule") { type = ScheduleNavType(gson) },
                navArgument(name = "isModify") { type = NavType.BoolType },
            )

        fun createRoute(
            targetSchedule: Schedule,
            isModify: Boolean = false,
        ) = "ScheduleCreate/${Uri.encode(gson.toJson(targetSchedule))}/$isModify"
    }

    // 일상 업로드
    data object DailyUpload : ScreenDestinations(route = "DailyUpload")

    // 갤러리
    data object Gallery : ScreenDestinations(route = "Gallery")

    // 앨범
    data object Album : ScreenDestinations(route = "Album")

    // 관심사 공유
    data object Interest : ScreenDestinations(route = "Interest")

    // 관심사 공유 목록
    data object InterestList : ScreenDestinations(route = "InterestList")

    // 선정 안 된 관심사
    data object OtherInterest : ScreenDestinations(route = "OtherInterest")

    // 알림
    data object Notification : ScreenDestinations(route = "Notification")

    // 마이페이지
    data object MyPage : ScreenDestinations(route = "MyPage")

    // 닉네임 변경
    data object EditName : ScreenDestinations(route = "EditName")

    // 답변 작성
    data object AnswerWrite : ScreenDestinations(route = "AnswerWrite")

    data object PastQuestion : ScreenDestinations(route = "pastQuestion") {
        override val route: String
            get() = "pastQuestion/{questionId}"
        val arguments = listOf(
            navArgument("questionId") { type = NavType.LongType }
        )
        fun createRoute(questionId: Long) = "pastQuestion/$questionId"
    }
}

// Schedule 전달 위한 NavType 정의
class ScheduleNavType(
    private val gson: Gson,
) : NavType<Schedule>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): Schedule? = bundle.getParcelable(key)

    override fun parseValue(value: String): Schedule = gson.fromJson(value, Schedule::class.java)

    override fun put(
        bundle: Bundle,
        key: String,
        value: Schedule,
    ) {
        bundle.putParcelable(key, value)
    }
}
