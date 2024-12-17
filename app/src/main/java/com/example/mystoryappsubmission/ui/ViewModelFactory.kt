package com.example.mystoryappsubmission.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryappsubmission.di.Injection
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.repository.StoryRepository
import com.example.mystoryappsubmission.ui.add.AddStoryViewModel
import com.example.mystoryappsubmission.ui.detail.DetailStoryViewModel
import com.example.mystoryappsubmission.ui.login.LoginViewModel
import com.example.mystoryappsubmission.ui.maps.MapsViewModel
import com.example.mystoryappsubmission.ui.story.StoryViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository, userPreference) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepository, userPreference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userPreference, application) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(userPreference) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(userPreference, application) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.providePreference(context),
                    context.applicationContext as Application
                ).also { instance = it }
            }
    }
}








