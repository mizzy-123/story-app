package com.bangkit.storyapp.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<ListStory>
)

@Entity(tableName = "story")
data class ListStory(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float?,
    val lon: Float?
)