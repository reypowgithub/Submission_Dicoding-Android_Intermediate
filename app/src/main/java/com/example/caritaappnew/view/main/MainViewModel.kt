package com.example.caritaappnew.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.userPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref : userPreference) : ViewModel()   {

    fun getUser() : LiveData<UserModel> {
        return pref.getData().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logoutUser()
        }
    }
}