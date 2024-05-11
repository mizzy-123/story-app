package com.bangkit.storyapp.api

import com.bangkit.storyapp.data.model.response.LoginResponse
import com.bangkit.storyapp.data.model.response.RegisterResponse
import com.bangkit.storyapp.data.model.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun testLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    fun postStories(
        @Path("description") description: String,
        @Path("lat") lat: Float? = null,
        @Path("lon") lon: Float? = null
    ): Call<StoriesResponse>
}