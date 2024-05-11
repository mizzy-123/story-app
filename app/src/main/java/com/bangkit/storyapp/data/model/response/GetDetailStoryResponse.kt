package com.bangkit.storyapp.data.model.response

data class GetDetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float,
    val lon: Float
)
