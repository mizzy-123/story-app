package com.bangkit.storyapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.LoginResponse
import com.bangkit.storyapp.data.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel: ViewModel() {
    fun login(con: Context, email: String, password: String){
        RetrofitClient.instance.login(email, password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(con, "Login successfull", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
                    Toast.makeText(con, "Register successfull", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(con, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}