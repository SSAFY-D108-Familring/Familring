package com.familring.presentation.screen.signup

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.familring.presentation.R
import com.familring.presentation.component.BrownRoundButton
import com.familring.presentation.component.LoadingDialog
import com.familring.presentation.theme.Black
import com.familring.presentation.theme.Gray01
import com.familring.presentation.theme.Gray02
import com.familring.presentation.theme.Gray03
import com.familring.presentation.theme.Green03
import com.familring.presentation.theme.Typography
import com.familring.presentation.theme.White
import com.familring.presentation.util.noRippleClickable
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun DoneRoute(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    navigateToHome: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.makeFamily()
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.Error -> {
                    showSnackBar(event.message)
                }

                else -> {}
            }
        }
    }

    DoneScreen(
        modifier = modifier,
        navigateToHome = navigateToHome,
        code = uiState.familyCode,
        showSnackBar = showSnackBar,
    )

    if (uiState.isLoading) {
        LoadingDialog()
    }
}

@Composable
fun DoneScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    code: String = "",
    showSnackBar: (String) -> Unit = {},
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Text(
                text = "완료!",
                style = Typography.titleLarge.copy(fontSize = 45.sp),
                color = Green03,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.45f)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_mailbox),
                contentDescription = "img_family",
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Text(
                text = "코드를 공유하고",
                style = Typography.displayLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Text(
                text = "가족을 초대해 보세요!",
                style = Typography.displayLarge.copy(fontSize = 24.sp),
                color = Black,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(
                modifier =
                    Modifier.run {
                        fillMaxWidth(0.82f)
                            .wrapContentSize()
                            .background(color = White)
                            .border(width = 2.dp, color = Gray03, shape = RoundedCornerShape(15.dp))
                    },
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 20.dp, horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "나의 초대코드",
                        color = Gray02,
                        style = Typography.displayMedium.copy(fontSize = 18.sp),
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = code,
                        style =
                            Typography.headlineMedium.copy(
                                fontSize = 28.sp,
                                letterSpacing = 0.em,
                            ),
                        color = Black,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    BrownRoundButton(onClick = {
                        shareCode(code, context, showSnackBar)
                    }, text = "카카오톡으로 공유하기")
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            Text(
                modifier = Modifier.noRippleClickable { navigateToHome() },
                text = "홈으로 이동할게요",
                style = Typography.displayLarge.copy(fontSize = 18.sp),
                color = Gray01,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

fun shareCode(
    code: String,
    context: Context,
    showSnackBar: (String) -> Unit,
) {
    val codeMessage =
        FeedTemplate(
            content =
                Content(
                    title = "가족에게서 코드가 도착했어요! $code",
                    description = "패밀링에서 코드를 입력하고, 가족과 소통해 보세요.",
                    imageUrl = "https://contents.creators.mypetlife.co.kr/content/uploads/2020/03/19135734/thumb-20200319125759_851795890399d09add3c23661fc38b19_pocq_700x524.png",
                    link = Link(),
                ),
            buttons =
                listOf(
                    Button(
                        "앱 실행하고 코드 복사하기",
                        Link(
                            androidExecutionParams = mapOf("action" to "copy_code", "code" to code),
                        ),
                    ),
                ),
        )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        // 카카오톡으로 공유 가능
        ShareClient.instance.shareDefault(context, codeMessage) { result, error ->
            if (error != null) {
                showSnackBar("카카오톡 공유 실패")
            } else if (result != null) {
                showSnackBar("카카오톡 공유 성공! ${result.intent}")
                startActivity(context, result.intent, null)

                // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                Timber.d("Warning Msg: ${result.warningMsg}")
                Timber.d("Argument Msg: ${result.argumentMsg}")
            }
        }
    } else {
        // 카카오톡 미설치: 웹 공유 사용 권장
        val shareUrl = WebSharerClient.instance.makeDefaultUrl(codeMessage)

        // CustomTabs로 웹 브라우저 열기
        // 1. CustomTabServiceConnection 지원 부라우저 열기
        // Chrome, 삼성 인터넷 등
        try {
            KakaoCustomTabsClient.openWithDefault(context, shareUrl)
        } catch (e: UnsupportedOperationException) {
            Timber.d("Web share error: ${e.message}")
        }

        // 2. CustomTabsServiceConnection 미지원 브라우저 열기
        // 다음, 네이버 등
        try {
            KakaoCustomTabsClient.open(context, shareUrl)
        } catch (e: ActivityNotFoundException) {
            // 디바이스에 설치된 인터넷 브라우저 없을 때
            showSnackBar("인터넷 브라우저를 설치해 주세요.")
        }
    }
}

@Preview
@Composable
fun DoneScreenPreview() {
    DoneScreen()
}
