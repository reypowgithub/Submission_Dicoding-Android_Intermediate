package com.example.caritaappnew.model.pagging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.caritaappnew.model.api.ApiService
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.model.respon.UpdateStoryResponse

class StoryRepo(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun getStories(token : String) : LiveData<PagingData<Stories>> {
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