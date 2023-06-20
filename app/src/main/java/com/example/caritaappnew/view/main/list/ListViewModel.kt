package com.example.caritaappnew.view.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.pagging.StoryRepo
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.model.userPreference

class ListViewModel(private val repository: StoryRepo, private val pref: userPreference): ViewModel() {

    fun story(token: String): LiveData<PagingData<Stories>> =
        repository.getStories(token).cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return pref.getData().asLiveData()
    }
}