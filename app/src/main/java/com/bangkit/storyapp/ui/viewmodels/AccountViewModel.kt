package com.bangkit.storyapp.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.LoginResponse
import com.bangkit.storyapp.data.model.response.RegisterResponse
import com.bangkit.storyapp.ui.activities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel: ViewModel() {
    fun login(con: Context, email: String, password: String, callback: (String?) -> Unit){
        RetrofitClient.instance.login(email, password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseLogin = response.body()
                    val tokenResponse = responseLogin?.loginResult?.token
                    Toast.makeText(con, "Login successfull", Toast.LENGTH_SHORT).show()
                    callback(tokenResponse)
                } else {
                    callback(null)
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(con, "Failure: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null)
                Toast.makeText(con, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun register(con: Context, username: String, email: String, password: String){
        RetrofitClient.instance.register(username, email, password).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                if (response.isSuccessful){
                    val intent = Intent(con, LoginActivity::class.java)
                    con.startActivity(intent)
                    Toast.makeText(con, "Register successfull Silahkan Login...", Toast.LENGTH_LONG).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(con, "Failure: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(con, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}