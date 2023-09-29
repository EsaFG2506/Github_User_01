package com.dicoding.githubuser.detail

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.githubuser.data.local.DbModule
import com.dicoding.githubuser.data.response.GithubDetailResponse
import com.dicoding.githubuser.data.response.UserItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val db: DbModule) : ViewModel() {

    private val _users = MutableLiveData<GithubDetailResponse>()
    val users: LiveData<GithubDetailResponse> get() = _users

    private val _followers = MutableLiveData<List<UserItem>>()
    val followers: LiveData<List<UserItem>> get() = _followers

    private val _followings = MutableLiveData<List<UserItem>>()
    val followings: LiveData<List<UserItem>> get() = _followings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _resultDeleteFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite: LiveData<Boolean> get() = _resultDeleteFavorite

    private val _resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultSuksesFavorite: LiveData<Boolean> get() = _resultSuksesFavorite


    init {
        _isFavorite.value = false
    }

    fun setFavorite(item: UserItem?) {
        viewModelScope.launch {
            item?.let {
                if (_isFavorite.value == true) {
                    db.userDao.delete(item)
                    _resultDeleteFavorite.postValue(true)
                } else {
                    db.userDao.insert(item)
                    _resultSuksesFavorite.postValue(true)
                }
            }
            _isFavorite.value = !_isFavorite.value!!
        }
    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                _isFavorite.postValue(true)
            }
        }
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        ApiConfig.apiService
            .getUserDetail(username)
            .enqueue(object : Callback<GithubDetailResponse> {
                override fun onResponse(
                    call: Call<GithubDetailResponse>,
                    response: Response<GithubDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
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
                override fun onResponse(
                    call: Call<List<UserItem>>,
                    response: Response<List<UserItem>>
                ) {
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

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}