package com.example.caritaappnew.view.singup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.model.api.ApiConfig
import com.example.caritaappnew.model.respon.CommonResponse
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingUpViewModel : ViewModel() {

    private val _load = MutableLiveData<Boolean>()

    fun singupUser(name : String, email : String, password: String, apicallback: ApiCallback){
        _load.value = true
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                _load.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error)
                        apicallback.onResponse(response.body() != null, "success")
                } else {
                    _load.value = true
                }
                Log.e("SingUpViewModel", "onFailure : ${response.message()}")
                response.errorBody()?.let {
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    apicallback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _load.value = false
                Log.e("SingUpViewModel", "onFailure : ${t.message}")
                apicallback.onResponse(false, t.message.toString())
            }

        })
    }

}