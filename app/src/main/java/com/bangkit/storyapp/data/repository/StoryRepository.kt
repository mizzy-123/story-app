package com.bangkit.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.*
import com.bangkit.storyapp.api.ApiService
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.data.paging.StoryRemoteMediator
import com.bangkit.storyapp.database.StoryDatabase

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token: String
) {
    fun getStory(): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }

        ).liveData
    }
}