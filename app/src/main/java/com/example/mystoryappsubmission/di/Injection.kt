package com.example.mystoryappsubmission.di

import android.content.Context
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.repository.StoryRepository
import com.example.mystoryappsubmission.ui.dataStore
import com.example.mystoryappsubmission.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return StoryRepository(apiService, pref)
    }

    fun providePreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }
}
