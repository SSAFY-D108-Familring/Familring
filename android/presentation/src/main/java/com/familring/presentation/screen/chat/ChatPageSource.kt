package com.familring.presentation.screen.chat

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.chat.Chat
import com.familring.domain.repository.FamilyRepository
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 0

class ChatPageSource
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
        private val authDataStore: AuthDataStore,
    ) : PagingSource<Int, Chat>() {
        override fun getRefreshKey(state: PagingState<Int, Chat>): Int? = state.anchorPosition

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> =
            try {
                val page = params.key ?: STARTING_PAGE_INDEX
                val familyId = authDataStore.getFamilyId()
                val userId = authDataStore.getUserId()

                when (
                    val response =
                        familyRepository
                            .enterRoom(
                                roomId = familyId ?: -1,
                                userId = userId ?: -1,
                                page = page,
                                size = params.loadSize,
                            ).first()
                ) {
                    is ApiResponse.Success -> {
                        val data = response.data
                        if (data != emptyList<PagingData<Chat>>()) {
                            LoadResult.Page(
                                data = data,
                                prevKey =
                                    when (page) {
                                        STARTING_PAGE_INDEX -> null
                                        else -> page - 1
                                    },
                                nextKey = page + 1,
                            )
                        } else {
                            LoadResult.Invalid()
                        }
                    }

                    is ApiResponse.Error -> {
                        LoadResult.Error(Exception(response.message))
                    }
                }
            } catch (e: ApiException) {
                LoadResult.Error(e)
            }
    }
