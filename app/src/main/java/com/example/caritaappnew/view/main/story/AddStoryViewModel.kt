package com.example.caritaappnew.view.main.story

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.api.ApiConfig
import com.example.caritaappnew.model.respon.CommonResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {
    private val _load = MutableLiveData<Boolean>()

    fun upload(user : UserModel, description : String, imageMultipart : MultipartBody.Part, apicallback : ApiCallback){
        _load.value = true
        val client = ApiConfig.getApiService()
            .addStory("Bearer ${user.token}",imageMultipart,description)

        client.enqueue(object : Callback<CommonResponse> {
            @Suppress("CAST_NEVER_SUCCEEDS")
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        apicallback.onResponse(response.body() != null, "success")
                    }
                } else {
                    try {
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextClean() as? JSONObject
                        val message = jsonObject?.getString("message") ?: "An error occurred"
                        apicallback.onResponse(false, message)
                    } catch (e: Exception) {
                        Log.e("AddStoryViewModel", "Error parsing JSON: ${e.message}")
                        apicallback.onResponse(false, "An error occurred")
                    }
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _load.value = false
                Log.e("AddStoryViewModel", "onFailure: ${t.message}")
                apicallback.onResponse(false, t.message.toString())
            }

        })

    }
}