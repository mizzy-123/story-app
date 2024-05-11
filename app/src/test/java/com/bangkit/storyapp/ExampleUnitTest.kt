package com.bangkit.storyapp

import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.model.response.LoginResponse
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println("heelo")
    }

    @Test
    fun testAPi(){
        RetrofitClient.instance.login("mizzy@gmail.com", "1234567890").enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseLogin = response.body()
                    println("Response message: ${responseLogin?.message}") // Menampilkan pesan response
                } else {
                    println("Error: ${response.code()}") // Menampilkan kode error jika tidak berhasil
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("Failure: ${t.message}") // Menampilkan pesan failure jika terjadi kegagalan dalam koneksi atau permintaan
            }
        })
    }
}