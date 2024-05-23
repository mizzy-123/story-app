package com.bangkit.storyapp

import com.bangkit.storyapp.data.model.response.ListStory

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..100){
            val story = ListStory(
                id = i.toString(),
                name = "name + $i",
                description = "description + $i",
                photoUrl = "photoUrl + $i",
                createdAt = "createdAt + $i",
                lat = i.toFloat(),
                lon = i.toFloat()
            )
            items.add(story)
        }
        return items
    }
}