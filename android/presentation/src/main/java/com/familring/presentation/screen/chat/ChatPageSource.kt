package com.familring.presentation.screen.chat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.chat.Chat
import com.familring.domain.repository.FamilyRepository
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChatPageSource
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
        private val authDataStore: AuthDataStore,
    ) : PagingSource<Int, Chat>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> =
            try {
                val page = params.key ?: 0
                val familyId = authDataStore.getFamilyId()
                val userId = authDataStore.getUserId()

                when (
                    val response =
                        familyRepository
                            .enterRoom(
                                roomId = familyId ?: -1,
                                userId = userId ?: -1,
                                page = page,
                                size = 30,
                            ).first()
                ) {
                    is ApiResponse.Success -> {
                        val data = response.data
                        LoadResult.Page(
                            data = data.chatList,
                            prevKey = if (page == 0) null else page - 1, // 이전 페이지 번호 설정
                            nextKey = if (!data.hasNext) null else page + 1, // 다음 페이지 번호 설정
                        )
                    }

                    is ApiResponse.Error -> {
                        LoadResult.Error(Exception(response.message))
                    }
                }
            } catch (e: ApiException) {
                LoadResult.Error(e)
            }

        override fun getRefreshKey(state: PagingState<Int, Chat>): Int? = state.anchorPosition
    }
