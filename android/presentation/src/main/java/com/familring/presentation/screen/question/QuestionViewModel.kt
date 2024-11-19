package com.familring.presentation.screen.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.TutorialDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.question.QuestionList
import com.familring.domain.repository.QuestionRepository
import com.familring.domain.repository.UserRepository
import com.familring.presentation.screen.gallery.TutorialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
    @Inject
    constructor(
        private val questionRepository: QuestionRepository,
        val userRepository: UserRepository,
        private val tutorialDataStore: TutorialDataStore,
    ) : ViewModel() {
        private val _tutorialUiState = MutableStateFlow(TutorialUiState())
        val tutorialUiState = _tutorialUiState.asStateFlow()

        private val _questionState = MutableStateFlow<QuestionState>(QuestionState.Loading)
        val questionState = _questionState.asStateFlow()

        private val _questionEvent = MutableSharedFlow<QuestionEvent>()
        val questionEvent = _questionEvent.asSharedFlow()

        private val _questionListState = MutableStateFlow<QuestionListState>(QuestionListState.Loading)
        val questionListState = _questionListState.asStateFlow()

        private var currentOrder = "desc"
        private var currentPage = 0

        private val _refreshTrigger = MutableStateFlow(0)

        init {
            getReadTutorial()
            viewModelScope.launch {
                _refreshTrigger.collectLatest {
                    getQuestion()
                }
            }
        }

        private fun getReadTutorial() {
            viewModelScope.launch {
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = tutorialDataStore.getQuestionReadTutorial(),
                    )
                }
            }
        }

        fun setReadTutorial() {
            viewModelScope.launch {
                tutorialDataStore.setQuestionReadTutorial(true)
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = true,
                    )
                }
            }
        }

        fun setReadTutorialState(isRead: Boolean) {
            viewModelScope.launch {
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = isRead,
                    )
                }
            }
        }

        fun refresh() {
            _refreshTrigger.value += 1
        }

        fun getQuestion(questionId: Long? = null) {
            viewModelScope.launch {
                questionRepository.getQuestion(questionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _questionState.value =
                                QuestionState.Success(
                                    questionId = response.data.questionId,
                                    questionContent = response.data.questionContent,
                                    answerContents = response.data.items,
                                )
                        }

                        is ApiResponse.Error -> {
                            _questionState.value =
                                QuestionState.Error(
                                    errorMessage = response.message,
                                )
                        }
                    }
                }
            }
        }

        fun postAnswer(content: String) {
            Timber.d("작성 요청 들어감")
            viewModelScope.launch {
                questionRepository.postAnswer(content).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _questionEvent.emit(QuestionEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _questionEvent.emit(
                                QuestionEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                            Timber.d("code: ${response.code}, message: ${response.message}")
                        }
                    }
                }
            }
        }

        fun patchAnswer(content: String) {
            Timber.d("수정 요청 들어감")
            viewModelScope.launch {
                questionRepository.patchAnswer(content).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _questionEvent.emit(QuestionEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _questionEvent.emit(
                                QuestionEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                            Timber.d("code: ${response.code}, message: ${response.message}")
                        }
                    }
                }
            }
        }

        fun getAllQuestion(order: String = currentOrder) {
            currentOrder = order
            currentPage = 0

            viewModelScope.launch {
                _questionListState.value = QuestionListState.Loading
                questionRepository.getAllQuestion(currentPage, currentOrder).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _questionListState.value =
                                QuestionListState.Success(
                                    questionList = response.data,
                                )
                        }

                        is ApiResponse.Error -> {
                            _questionListState.value =
                                QuestionListState.Error(
                                    errorMessage = response.message,
                                )
                            Timber.d("code: ${response.code}, message: ${response.message}")
                        }
                    }
                }
            }
        }

        fun loadNextPage() {
            val currentState = _questionListState.value
            if (currentState is QuestionListState.Success && !currentState.questionList.last) {
                currentPage++
                viewModelScope.launch {
                    questionRepository
                        .getAllQuestion(currentPage, currentOrder)
                        .collectLatest { response ->
                            when (response) {
                                is ApiResponse.Success -> {
                                    val currentItems = currentState.questionList.items
                                    _questionListState.value =
                                        QuestionListState.Success(
                                            QuestionList(
                                                pageNo = response.data.pageNo,
                                                hasNext = response.data.hasNext,
                                                items = currentItems + response.data.items,
                                                last = response.data.last,
                                            ),
                                        )
                                }

                                is ApiResponse.Error -> {
                                    _questionListState.value =
                                        QuestionListState.Error(
                                            errorMessage = response.message,
                                        )
                                }
                            }
                        }
                }
            }
        }

        fun knockKnock(
            questionId: Long,
            receiverId: Long,
        ) {
            viewModelScope.launch {
                questionRepository.knockKnock(questionId, receiverId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _questionEvent.emit(QuestionEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _questionEvent.emit(
                                QuestionEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                            Timber.d("code: ${response.code}, message: ${response.message}")
                        }
                    }
                }
            }
        }
    }
