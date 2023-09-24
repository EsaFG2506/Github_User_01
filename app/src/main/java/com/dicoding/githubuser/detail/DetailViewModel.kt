package com.dicoding.githubuser.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.GithubDetailResponse
import com.dicoding.githubuser.data.response.UserItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _users = MutableLiveData<GithubDetailResponse>()
    val users: LiveData<GithubDetailResponse> get() = _users

    private val _followers = MutableLiveData<List<UserItem>>()
    val followers: LiveData<List<UserItem>> get() = _followers

    private val _followings = MutableLiveData<List<UserItem>>()
    val followings: LiveData<List<UserItem>> get() = _followings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun setUserDetail(username : String) {
        _isLoading.value = true
        ApiConfig.apiService
            .getUserDetail(username)
            .enqueue(object : Callback<GithubDetailResponse> {
                override fun onResponse(
                    call: Call<GithubDetailResponse>,
                    response: Response<GithubDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _users.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<GithubDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${t.message}")
                    t.printStackTrace()
                }

            })
    }

    fun loadFollowers(username: String) {
        _isLoading.value = true
        ApiConfig.apiService
            .getFollowers(username)
            .enqueue(object : Callback<List<UserItem>> {
                override fun onResponse(
                    call: Call<List<UserItem>>,
                    response: Response<List<UserItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _followers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
    }

    fun loadFollowings(username: String) {
        _isLoading.value = true
        ApiConfig.apiService
            .getFollowing(username)
            .enqueue(object : Callback<List<UserItem>> {
                override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _followings.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
    }
}