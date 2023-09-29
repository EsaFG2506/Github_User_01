package com.dicoding.githubuser.data.retrofit


import com.dicoding.githubuser.data.response.GithubDetailResponse
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.UserItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getUser(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GithubDetailResponse>

    @GET("/users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("/users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}