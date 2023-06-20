package com.example.caritaappnew.model.api

import com.example.caritaappnew.model.respon.CommonResponse
import com.example.caritaappnew.model.respon.LoginResponse
import com.example.caritaappnew.model.respon.StoryResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun listStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size : Int,
    ): StoryResponse

    @GET("stories")
    fun listStorywithlocation(
        @Header("Authorization") token: String,
        @Query("size") loadSize: Int,
        @Query("location") location : Int
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") Authorization : String,
        @Part file : MultipartBody.Part,
        @Part("description") description : String
    ) : Call<CommonResponse>
}