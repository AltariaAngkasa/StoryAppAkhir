package com.example.mystoryappsubmission.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.ui.MainActivity
import com.example.mystoryappsubmission.databinding.LoginPageBinding
import com.example.mystoryappsubmission.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: LoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        initUI()
        observeViewModel()
        handleActions()
        startAnimations()
    }

    private fun initUI() {
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

    private fun observeViewModel() {
        viewModel.apply {
            statusMessage.observe(this@LoginActivity) { message ->
                message?.let {
                    showToast(it)
                    viewModel.clearStatusMessage()
                }
            }

            userToken.observe(this@LoginActivity) { token ->
                token?.let {
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun handleActions() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isBlank()) {
                    showErrorDialog(getString(R.string.email_must_not_be_empty))
                } else if (password.isBlank()) {
                    showErrorDialog(getString(R.string.password_must_not_be_empty))
                } else {
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error_login))
            setMessage(message)
            setPositiveButton(getString(R.string.ok), null)
            create()
            show()
        }
    }

    private fun startAnimations() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        AnimatorSet().apply {
            playSequentially(
                fadeIn(binding.titleTextView),
                fadeIn(binding.messageTextView),
                fadeIn(binding.emailTextView),
                fadeIn(binding.emailEditTextLayout),
                fadeIn(binding.passwordTextView),
                fadeIn(binding.passwordEditTextLayout),
                fadeIn(binding.loginButton)
            )
            startDelay = 100
        }.start()
    }

    private fun fadeIn(view: View) = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
        duration = 300
    }

    private fun navigateToMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}





