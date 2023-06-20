package com.example.caritaappnew.model.respon

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("eror")
    val eror: Boolean,

    @field:SerializedName("message")
    val message : String,

    @field:SerializedName("loginResult")
    val loginresult: LoginResult,
)

data class LoginResult(
    @field:SerializedName("userId")
    val userId : String,

    @field:SerializedName("name")
    val name : String,

    @field:SerializedName("token")
    val token : String
)