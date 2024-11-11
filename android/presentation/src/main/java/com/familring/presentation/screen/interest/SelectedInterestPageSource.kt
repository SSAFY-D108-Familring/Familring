package com.familring.presentation.screen.interest

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.interest.SelectedInterest
import com.familring.domain.repository.InterestRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SelectedInterestPageSource
    @Inject
    constructor(
        private val interestRepository: InterestRepository,
    ) : PagingSource<Int, SelectedInterest>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SelectedInterest> =
            try {
                val page = params.key ?: 0
                when (val result = interestRepository.getInterests(pageNo = page).first()) {
                    is ApiResponse.Success -> {
                        val data = result.data
                        LoadResult.Page(
                            data = data.selectedInterests,
                            prevKey = if (page == 0) null else page - 1,
                            nextKey = if (data.last) null else page + 1,
                        )
                    }

                    is ApiResponse.Error -> {
                        LoadResult.Error(Exception("API Error: ${result.message}"))
                    }
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }

        override fun getRefreshKey(state: PagingState<Int, SelectedInterest>): Int? = state.anchorPosition
    }
