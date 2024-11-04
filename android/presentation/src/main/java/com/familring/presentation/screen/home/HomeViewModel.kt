package com.familring.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.FamilyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
    ) : ViewModel() {
        private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
        val homeState = _homeState.asStateFlow()

        private val _familyState = MutableStateFlow<FamilyState>(FamilyState.Loading)
        val familyState = _familyState.asStateFlow()

        init {
            getFamilyMembers()
            getFamilyInfo()
        }

        private fun getFamilyMembers() {
            viewModelScope.launch {
                try {
                    familyRepository.getFamilyMembers().collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _homeState.value = HomeState.Success(response.data)
                            }

                            is ApiResponse.Error -> {
                                _homeState.value = HomeState.Error(response.message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _homeState.value = HomeState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
            }
        }

        private fun getFamilyInfo() {
            viewModelScope.launch {
                try {
                    familyRepository.getFamilyInfo().collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _familyState.value = FamilyState.Success(response.data)
                                Timber.d("${response.data}")
                            }

                            is ApiResponse.Error -> {
                                _familyState.value = FamilyState.Error(response.message)
                                Timber.tag("HomeViewModel").e("Error: " + response.message)
                            }
                        }
                    }
                } catch (
                    e: Exception,
                ) {
                    _familyState.value = FamilyState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
            }
        }
    }
