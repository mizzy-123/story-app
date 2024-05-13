package com.bangkit.storyapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.GetStoriesResponse
import com.bangkit.storyapp.data.model.response.ListStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel: ViewModel() {

    fun getStories(cont: Context, token: String, callback: (List<ListStory>?) -> Unit){
        RetrofitClient.instance.getStories("Bearer $token").enqueue(object : Callback<GetStoriesResponse>{
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>,
            ) {
                Log.d("token", token)
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    callback(dataResponse?.listStory)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(cont, "Failure: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("getStories", "Failure: $errorMessage")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                Toast.makeText(cont, "failure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("getStories", t.message.toString())
                callback(null)
            }

        })
    }
}