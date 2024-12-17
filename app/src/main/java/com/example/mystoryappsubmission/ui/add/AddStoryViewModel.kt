package com.example.mystoryappsubmission.ui.add

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryappsubmission.R
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.data.response.AddStoryResponse
import com.example.storyappsubmission.data.response.LoginResult
import com.example.mystoryappsubmission.data.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreference, private val app: Application) : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun uploadStory(token: String, image: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory(token, image, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _statusMessage.value = app.getString(R.string.add_story_success)
                } else {
                    _isSuccess.value = false
                    _statusMessage.value =
                        app.getString(R.string.failed_to_add_story, response.message())
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                _statusMessage.value = app.getString(R.string.error, t.message)
            }
        })
    }

    companion object {
        private const val TAG = "UploadStoryViewModel"
    }
}
