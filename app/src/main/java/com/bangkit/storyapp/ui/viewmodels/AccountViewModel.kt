package com.bangkit.storyapp.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.R
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.LoginResponse
import com.bangkit.storyapp.data.model.response.RegisterResponse
import com.bangkit.storyapp.ui.activities.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel: ViewModel() {
    private val _loadingRegister = MutableLiveData<Boolean>()
    val loadingRegister: LiveData<Boolean> = _loadingRegister
    private val _isAccountCreated = MutableLiveData<Boolean>()
    val isAccountCreated: LiveData<Boolean> = _isAccountCreated

    fun login(con: Context, email: String, password: String, callback: (String?) -> Unit){
        RetrofitClient.instance.login(email, password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseLogin = response.body()
                    val tokenResponse = responseLogin?.loginResult?.token
                    Toast.makeText(con, "Login successfull", Toast.LENGTH_SHORT).show()
                    callback(tokenResponse)
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            val errorMessage = jsonObject.getString("message")
                            println("Error: $errorMessage")
                            Toast.makeText(con, con.getString(R.string.alert_failure, errorMessage), Toast.LENGTH_SHORT).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(con, con.getString(R.string.alert_failure, "Error parsing error message"), Toast.LENGTH_SHORT).show()
                        }
                    }
                    callback(null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(con, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }

    fun register(con: Context, username: String, email: String, password: String){
        _loadingRegister.value = true
        _isAccountCreated.value = false
        RetrofitClient.instance.register(username, email, password).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                if (response.isSuccessful){
                    val intent = Intent(con, LoginActivity::class.java)
                    con.startActivity(intent)
                    _isAccountCreated.value = true
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(con, "Failure: $errorMessage", Toast.LENGTH_SHORT).show()
                }

                _loadingRegister.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(con, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                _loadingRegister.value = true
            }

        })
    }
}