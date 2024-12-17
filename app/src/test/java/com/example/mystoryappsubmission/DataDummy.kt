package com.example.mystoryappsubmission

import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.response.StoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "Name $i",
                description = "Description $i",
                createdAt = "Created At $i",
                photoUrl = "photoUrl $i",
                lon = i.toDouble(),
                lat = i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}