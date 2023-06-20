package com.example.caritaappnew.view.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.api.ApiConfig
import com.example.caritaappnew.model.respon.LoginResponse
import com.example.caritaappnew.model.userPreference
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: userPreference) : ViewModel() {
    private val _load = MutableLiveData<Boolean>()

    fun loginUser(email : String, password : String, apicallback : ApiCallback){
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.eror){apicallback.onResponse(response.body() != null, "success")
                        apicallback.onResponse(response.body() != null, "Success")
                        val user = UserModel(
                            responseBody.loginresult.name,
                            email,
                            password,
                            responseBody.loginresult.userId,
                            responseBody.loginresult.token,
                            true
                        )
                        viewModelScope.launch {
                            pref.saveData(user)
                        }
                    }
                } else{
                    Log.e("LoginViewModel", "onFailure : ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    apicallback.onResponse(false, message)
                }
                _load.value = false

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _load.value = true
                Log.d("Failed",t.message.toString())
                _load.value = false
            }
        })
    }
}