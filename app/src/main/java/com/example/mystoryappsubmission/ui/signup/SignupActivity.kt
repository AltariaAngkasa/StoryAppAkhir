package com.example.mystoryappsubmission.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.databinding.SignupPageBinding
import com.example.storyappsubmission.data.response.SignupResponse
import com.example.mystoryappsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: SignupPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupPageBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        initializeUI()
        handleUserActions()
        startViewAnimations()
    }

    private fun initializeUI() {
        with(window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
        supportActionBar?.hide()
    }

    private fun handleUserActions() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                showErrorDialog(getString(R.string.all_fields_must_be_filled))
            } else {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        ApiConfig.getApiService().register(name, email, password).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val message = getString(
                            R.string.account_with_has_been_created,
                            email
                        )
                        showSuccessDialog(message)
                    } ?: run {
                        showErrorDialog(getString(R.string.registration_failed_response_body_is_null))
                    }
                } else {
                    showErrorDialog(
                        getString(
                            R.string.registration_failed,
                            response.errorBody()?.string() ?: response.message()
                        ))
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showErrorDialog(
                    getString(
                        R.string.registration_failed_2,
                        t.localizedMessage ?: getString(R.string.unknown_error)
                    ))
            }
        })
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage(message)
            setPositiveButton("Continue") { _, _ -> finish() }
            create()
            show()
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun startViewAnimations() {
        binding.imageView.animateHorizontalSwing()

        AnimatorSet().apply {
            playSequentially(
                binding.titleTextView.fadeIn(),
                binding.nameTextView.fadeIn(),
                binding.nameEditTextLayout.fadeIn(),
                binding.emailTextView.fadeIn(),
                binding.emailEditTextLayout.fadeIn(),
                binding.passwordTextView.fadeIn(),
                binding.passwordEditTextLayout.fadeIn(),
                binding.signupButton.fadeIn()
            )
            startDelay = 100
        }.start()
    }

    private fun View.fadeIn(duration: Long = 300): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).apply {
            this.duration = duration
        }
    }

    private fun View.animateHorizontalSwing() {
        ObjectAnimator.ofFloat(this, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}

