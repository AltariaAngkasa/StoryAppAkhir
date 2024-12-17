package com.example.mystoryappsubmission.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.storyappsubmission.data.response.LoginResponse
import com.example.mystoryappsubmission.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference, private val app: Application) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userToken = MutableLiveData<String>()
    val userToken: LiveData<String> = _userToken

    private val _isLoading = MutableLiveData<Boolean>()

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _userName.value = responseBody?.loginResult?.name.orEmpty()
                    _userId.value = responseBody?.loginResult?.userId.orEmpty()
                    _userToken.value = responseBody?.loginResult?.token.orEmpty()

                    viewModelScope.launch {
                        pref.saveUser(
                            _userName.value.orEmpty(),
                            _userId.value.orEmpty(),
                            "Bearer " + _userToken.value.orEmpty()
                        )
                    }

                    _statusMessage.value = app.getString(R.string.login_successful)
                    Log.d(TAG, _userToken.value.orEmpty())
                } else {
                    _statusMessage.value =
                        app.getString(R.string.login_failed_please_ensure_the_email_and_password_are_valid)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _statusMessage.value = "Error: ${t.message}"
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}




