package com.familring.presentation.screen.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
    @Inject
    constructor(
        val questionRepository: QuestionRepository,
    ) : ViewModel() {
        private val _questionState = MutableStateFlow<QuestionState>(QuestionState.Loading)
        val questionState = _questionState.asStateFlow()

        private val _questionEvent = MutableSharedFlow<QuestionEvent>()
        val questionEvent = _questionEvent.asSharedFlow()

        private val _refreshTrigger = MutableStateFlow(0)

        init {
            viewModelScope.launch {
                _refreshTrigger.collectLatest {
                    getQuestion()
                }
            }
        }

        fun refresh() {
            _refreshTrigger.value += 1
        }

        private fun getQuestion() {
            viewModelScope.launch {
                questionRepository.getQuestion().collectLatest { response ->
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
                            Timber.d("code: ${response.code}, message: ${response.message}")
                        }
                    }
                }
            }
        }

        fun postAnswer(content: String) {
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

        fun patchAnswer(
            answerId: Long,
            content: String,
        ) {
            viewModelScope.launch {
                questionRepository.patchAnswer(answerId, content).collectLatest { response ->
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
