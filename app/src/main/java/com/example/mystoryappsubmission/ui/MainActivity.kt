package com.example.mystoryappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.databinding.ActivityMainBinding
import com.example.mystoryappsubmission.di.Injection
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.repository.StoryRepository
import com.example.mystoryappsubmission.ui.add.AddStoryFragment
import com.example.mystoryappsubmission.ui.login.LoginActivity
import com.example.mystoryappsubmission.ui.maps.MapsFragment
import com.example.mystoryappsubmission.ui.story.StoryFragment

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var storyRepository: StoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userPreference = UserPreference.getInstance(dataStore)
        storyRepository = Injection.provideRepository(this)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[MainViewModel::class.java]

        setupBottomNavigation()

        mainViewModel.isLoading.observe(this, Observer { isLoading ->

        })


    }

    private fun setupBottomNavigation() {
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_mystory -> {
                    loadFragment(StoryFragment())
                    true
                }
                R.id.navigation_uploadstory -> {
                    loadFragment(AddStoryFragment())
                    true
                }
                R.id.navigation_maps -> {
                    loadFragment(MapsFragment())
                    true
                }
                R.id.navigation_logout -> {
                    mainViewModel.logout()
                    observeLogout()
                    true
                }
                else -> false
            }
        }


        loadFragment(StoryFragment())
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_activity, fragment)
            .commit()
    }

    private fun observeLogout() {

        mainViewModel.logoutStatus.observe(this, Observer { loggedOut ->
            if (loggedOut) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })
    }

}


