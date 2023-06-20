package com.example.caritaappnew.model.pagging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caritaappnew.model.respon.Stories

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Stories>)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, Stories>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}