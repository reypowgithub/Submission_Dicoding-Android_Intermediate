package com.example.caritaappnew.view.main.detail

import androidx.lifecycle.ViewModel
import com.example.caritaappnew.model.respon.Stories

class DetailViewModel : ViewModel() {
    lateinit var itemStory : Stories

    fun detailStory(story : Stories) : Stories {
        itemStory = story
        return itemStory
    }
}