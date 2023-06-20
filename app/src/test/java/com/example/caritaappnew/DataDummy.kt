package com.example.caritaappnew

import com.example.caritaappnew.model.respon.Stories

object DataDummy {
    fun generateDummyStoryResponse() : List<Stories>{
        val items : MutableList<Stories> = arrayListOf()
        for (i in 0..100){
            val story = Stories(
                "photoUrl",
                "2023-06-20T23:20:30.332Z",
                "Story ke $i",
                "Ini story ke $i",
                0.0,
                "",
                0.0
            )
            items.add(story)
        }
        return items
    }
}