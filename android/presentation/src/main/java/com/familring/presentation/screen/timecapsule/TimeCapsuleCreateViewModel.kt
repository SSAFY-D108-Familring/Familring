package com.familring.presentation.screen.timecapsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.TimeCapsuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TimeCapsuleCreateViewModel
    @Inject
    constructor(
        private val timeCapsuleRepository: TimeCapsuleRepository,
    ) : ViewModel() {
        fun createTimeCapsule(openDate: String) {
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsule(openDate).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            // 이벤트 필요
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
