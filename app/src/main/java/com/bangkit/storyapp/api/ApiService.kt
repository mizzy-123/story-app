package com.bangkit.storyapp.api

import com.bangkit.storyapp.data.model.response.GetDetailStoryResponse
import com.bangkit.storyapp.data.model.response.GetStoriesResponse
import com.bangkit.storyapp.data.model.response.LoginResponse
import com.bangkit.storyapp.data.model.response.RegisterResponse
import com.bangkit.storyapp.data.model.response.PostStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<PostStoriesResponse>

    @GET("stories")
    fun getStories(@Header("Authorization") token: String): Call<GetStoriesResponse>

    @GET("stories/{id}")
    fun getDetailStory(@Header("Authorization") token: String, @Path("id") id: String): Call<GetDetailStoryResponse>

    @POST("stories/guest")
    fun postStoriesGuest(
        @Path("description") description: String,
        @Part photo: MultipartBody.Part?,
        @Path("lat") lat: Float? = null,
        @Path("lon") lon: Float? = null
    ): Call<PostStoriesResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): GetStoriesResponse
}