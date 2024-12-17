package com.example.mystoryappsubmission.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryappsubmission.databinding.DetailPageBinding
import com.example.mystoryappsubmission.ui.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: DetailPageBinding
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(ID).toString()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.getUser().observe(this) { user ->
            if (user != null && user.token.isNotEmpty()) {
                user.token.let { viewModel.getDetail(it, id) }
            }
        }

        viewModel.detailName.observe(this) { name ->
            binding.tvTitle.text = name
        }

        viewModel.detailDesc.observe(this) { description ->
            binding.tvDesc.text = description
        }

        viewModel.detailPhotoUrl.observe(this) { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .into(binding.imgItemPhoto)
        }
    }

    companion object {
        const val ID = "id"
    }
}