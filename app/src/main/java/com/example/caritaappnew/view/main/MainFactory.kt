package com.example.caritaappnew.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.model.userPreference

class MainFactory (private val pref : userPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            else-> throw IllegalAccessException("Tidak ditemukan VM Class : " + modelClass.name)

        }
    }
}