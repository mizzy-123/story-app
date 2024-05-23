package com.bangkit.storyapp.di

import android.content.Context
import com.bangkit.storyapp.api.RetrofitClient
import com.bangkit.storyapp.data.repository.StoryRepository
import com.bangkit.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = RetrofitClient.instance
        return StoryRepository(database, apiService, token)
    }
}