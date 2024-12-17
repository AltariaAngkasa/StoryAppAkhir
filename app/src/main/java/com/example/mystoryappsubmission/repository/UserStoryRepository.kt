package com.example.mystoryappsubmission.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryappsubmission.pref.PagingStory
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.retrofit.ApiService

class StoryRepository(private val apiService: ApiService, private val pref: UserPreference) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingStory(apiService, pref)
            }
        ).liveData
    }

}





