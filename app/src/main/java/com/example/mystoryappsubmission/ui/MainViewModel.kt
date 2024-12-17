package com.example.mystoryappsubmission.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryappsubmission.pref.PagingStory
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.repository.StoryRepository
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class MainViewModel(storyRepository: StoryRepository, private val userPreference: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> = _logoutStatus

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)


    fun getUser(): LiveData<LoginResult> {
        return userPreference.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.logout()
            _logoutStatus.postValue(true)
        }
    }
}
