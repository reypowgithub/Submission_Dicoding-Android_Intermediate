package com.example.caritaappnew.model

import android.content.Context
import com.example.caritaappnew.model.api.ApiConfig
import com.example.caritaappnew.model.pagging.StoryDatabase
import com.example.caritaappnew.model.pagging.StoryRepo

object Injection{

    fun provideRepository(context: Context) : StoryRepo {
        val database = StoryDatabase.getDatabase(context)
        val apiStoryDatabase = ApiConfig.getApiService()
        return StoryRepo(database, apiStoryDatabase)
    }
}