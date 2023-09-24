package com.dicoding.githubuser.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.UserItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<UserItem>>()
    val listUsers: LiveData<List<UserItem>> get() = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        setUsers()
    }

    private fun setUsers() {
        _isLoading.value = true
       ApiConfig.apiService
           .getUser("Esa")
           .enqueue(object : Callback<GithubResponse>{
               override fun onResponse(
                   call: Call<GithubResponse>,
                   response: Response<GithubResponse>
               ) {
                   _isLoading.value = false
                   if (response.isSuccessful){
                       _listUsers.postValue(response.body()?.items)
                   }
               }

               override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                   _isLoading.value = false
                   Log.e(TAG, "Error: ${t.message}")
                   t.printStackTrace()
               }

           })
    }

    fun searchUsers(query: String) {
        _isLoading.value = true
        ApiConfig.apiService.getUser(query).enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Error: ${t.message}")
                t.printStackTrace()
            }
        })
    }

}