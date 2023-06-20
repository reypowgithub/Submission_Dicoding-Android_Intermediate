package com.example.caritaappnew.view.main.maps

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caritaappnew.model.api.ApiConfig
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.model.respon.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsViewModel(application : Application) : AndroidViewModel(application) {
    val liststory = MutableLiveData<List<Stories>>()

    fun storyLocation(token : String){
        ApiConfig.getApiService().listStorywithlocation("Bearer $token", 50, 1)
            .enqueue(object : Callback<StoryResponse>{
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful){
                        liststory.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    Toast.makeText(getApplication(), "Failed to retrieve stories", Toast.LENGTH_SHORT).show()
                    Log.d("Failure : ", "${t.message}")
                }
            })
    }

    fun getMapStory() : LiveData<List<Stories>>{
        return liststory
    }
}