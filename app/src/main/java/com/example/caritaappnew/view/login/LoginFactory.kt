package com.example.caritaappnew.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.model.userPreference

class LoginFactory(private val pref: userPreference): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(pref) as T
        }
        throw IllegalAccessException("Tidak ditemukan VM Class : " + modelClass.name)
    }
}