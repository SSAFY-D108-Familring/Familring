package com.familring.presentation.screen.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.repository.InterestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InterestListViewModel
    @Inject
    constructor(
        private val interestRepository: InterestRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(InterestListUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<InterestUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun getSelectInterestsPagination(): Flow<PagingData<SelectedInterest>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 18,
                        enablePlaceholders = false,
                    ),
            ) {
                SelectedInterestPageSource(interestRepository)
            }.flow.cachedIn(viewModelScope)

        fun getInterestDetails(interestId: Long) {
            _uiState.update {
                it.copy(
                    detailInterests = listOf(),
                )
            }
            viewModelScope.launch {
                interestRepository.getInterestDetail(interestId).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    detailInterests = result.data,
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
