package com.example.mystoryappsubmission.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryappsubmission.pref.UserPreference
import com.example.mystoryappsubmission.data.response.DetailStoryResponse
import com.example.storyappsubmission.data.response.LoginResult
import com.example.mystoryappsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel (private val pref: UserPreference) : ViewModel() {

    private val _detailName = MutableLiveData<String>()
    val detailName: LiveData<String> = _detailName

    private val _detailDesc = MutableLiveData<String>()
    val detailDesc: LiveData<String> = _detailDesc

    private val _detailPhotoUrl = MutableLiveData<String>()
    val detailPhotoUrl: LiveData<String> = _detailPhotoUrl

    private val _detailId = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetail(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailStory(token, id)
        client.enqueue(object: Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false

                    _detailId.value = responseBody?.story?.id.toString()
                    _detailName.value = responseBody?.story?.name.toString()
                    _detailDesc.value = responseBody?.story?.description.toString()
                    _detailPhotoUrl.value = responseBody?.story?.photoUrl.toString()
                } else {
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    companion object{
        private const val TAG = "DetailStoryViewModel"
    }
}