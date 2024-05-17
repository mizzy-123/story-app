package com.bangkit.storyapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.R
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.GetDetailStoryResponse
import com.bangkit.storyapp.data.model.response.GetStoriesResponse
import com.bangkit.storyapp.data.model.response.ListStory
import com.bangkit.storyapp.data.model.response.PostStoriesResponse
import com.bangkit.storyapp.data.model.response.Story
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryViewModel: ViewModel() {

    private val _loadingPostStories = MutableLiveData<Boolean>()
    val loadingPostStories: LiveData<Boolean> = _loadingPostStories
    val _isStoriesCreated = MutableLiveData<Boolean>()
    val isStoriesCreated: LiveData<Boolean> = _isStoriesCreated

    private val _loadingGetDetailStory = MutableLiveData<Boolean>()
    val loadingGetDetailStory: LiveData<Boolean> = _loadingGetDetailStory
    private val _getDetailStory = MutableLiveData<Story>()
    val getDetailStory: LiveData<Story> = _getDetailStory


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
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            val errorMessage = jsonObject.getString("message")
                            println("Error: $errorMessage")
                            Toast.makeText(cont, cont.getString(R.string.alert_failure, errorMessage), Toast.LENGTH_SHORT).show()
                            Log.e("getStories", "Failure: $errorMessage")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(cont, cont.getString(R.string.alert_failure, "Error parsing error message"), Toast.LENGTH_SHORT).show()
                        }
                    }
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

    fun postStories(cont: Context, token: String, description: String, image: File?){

        _loadingPostStories.value = true
        _isStoriesCreated.value = false

        val mediaType = MediaType.parse("image/*")
        val requestFile: RequestBody = RequestBody.create(mediaType, image!!)
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", image.name, requestFile)
        val descriptionPart: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), description
        )

        RetrofitClient.instance.postStories("Bearer $token",descriptionPart, imagePart).enqueue(object : Callback<PostStoriesResponse>{
            override fun onResponse(
                call: Call<PostStoriesResponse>,
                response: Response<PostStoriesResponse>,
            ) {
                if (response.isSuccessful){
                    val responseData = response.body()
                    Toast.makeText(cont, responseData?.message.toString(), Toast.LENGTH_SHORT).show()
                    _loadingPostStories.value = false
                    _isStoriesCreated.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            val errorMessage = jsonObject.getString("message")
                            println("Error: $errorMessage")
                            Toast.makeText(cont, cont.getString(R.string.alert_failure, errorMessage), Toast.LENGTH_SHORT).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(cont, cont.getString(R.string.alert_failure, "Error parsing error message"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                _loadingPostStories.value = false
            }

            override fun onFailure(call: Call<PostStoriesResponse>, t: Throwable) {
                Toast.makeText(cont, cont.getString(R.string.alert_failure, t.message.toString()), Toast.LENGTH_SHORT).show()
                Log.e("post", t.message.toString())
                _loadingPostStories.value = false
            }

        })
    }

    fun getDetailStories(cont: Context, token: String, id: String, onMessage: (String) -> Unit){
        _loadingGetDetailStory.value = true
        RetrofitClient.instance.getDetailStory("Bearer $token", id).enqueue(object : Callback<GetDetailStoryResponse>{
            override fun onResponse(
                call: Call<GetDetailStoryResponse>,
                response: Response<GetDetailStoryResponse>,
            ) {
                if (response.isSuccessful){
                    val getDataStory = response.body()
                    if (getDataStory != null){
                        _getDetailStory.value = getDataStory.story
                    }
                    onMessage(cont.getString(R.string.alert_succes))
                } else {
                    onMessage(cont.getString(R.string.alert_failure, "Something wrong"))
                }

                _loadingGetDetailStory.value = false
            }

            override fun onFailure(call: Call<GetDetailStoryResponse>, t: Throwable) {
                _loadingGetDetailStory.value = false
                onMessage(cont.getString(R.string.alert_failure, t.message.toString()))
            }

        })
    }
}