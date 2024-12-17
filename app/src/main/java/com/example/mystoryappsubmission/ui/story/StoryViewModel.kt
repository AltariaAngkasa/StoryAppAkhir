package com.example.mystoryappsubmission.ui.story


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.repository.StoryRepository
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.response.LoginResult
import kotlinx.coroutines.launch

class StoryViewModel(
    storyRepository: StoryRepository,
    private val pref: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

}


