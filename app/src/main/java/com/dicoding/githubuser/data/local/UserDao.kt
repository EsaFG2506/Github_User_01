package com.dicoding.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.data.response.UserItem

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserItem)

    @Query("SELECT * FROM user")
    fun loadAll(): LiveData<List<UserItem>>

    @Query("SELECT * FROM user WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): UserItem

    @Delete
    fun delete(user: UserItem)
}