package com.bangkit.storyapp.data.model.response

data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<ListStory>
)

data class ListStory(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float?,
    val lon: Float?
)