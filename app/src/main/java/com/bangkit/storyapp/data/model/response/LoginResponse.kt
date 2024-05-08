package com.bangkit.storyapp.data.model.response

data class LoginResponse(
    val error: String,
    val message: String,
    val loginResult: loginResult
)

data class loginResult(
    val userId: String,
    val name: String,
    val token: String
)
