package com.example.mystoryappsubmission.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mystoryappsubmission.MainDispatcherRule
import com.example.mystoryappsubmission.repository.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var storyRepository: StoryRepository

}

