package com.example.caritaappnew.view.main.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.model.Injection.provideRepository
import com.example.caritaappnew.model.userPreference

class ListFactory (private val context: Context, private val pref: userPreference) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(provideRepository(context), pref) as T
        }
        throw IllegalAccessException("Tidak ditemukan VM Class : " + modelClass.name)
    }
}