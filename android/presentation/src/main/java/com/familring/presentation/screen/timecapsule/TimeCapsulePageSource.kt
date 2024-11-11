package com.familring.presentation.screen.timecapsule

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.timecapsule.TimeCapsule
import com.familring.domain.repository.TimeCapsuleRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TimeCapsulePageSource
    @Inject
    constructor(
        private val timeCapsuleRepository: TimeCapsuleRepository,
    ) : PagingSource<Int, TimeCapsule>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TimeCapsule> =
            try {
                val page = params.key ?: 0
                when (val result = timeCapsuleRepository.getTimeCapsules(pageNo = page).first()) {
                    is ApiResponse.Success -> {
                        val data = result.data
                        LoadResult.Page(
                            data = data.timeCapsules,
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

        override fun getRefreshKey(state: PagingState<Int, TimeCapsule>): Int? = state.anchorPosition
    }
