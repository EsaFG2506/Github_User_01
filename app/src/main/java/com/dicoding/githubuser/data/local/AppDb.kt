package com.dicoding.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.githubuser.data.response.UserItem

@Database(entities = [UserItem::class], version = 1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    abstract fun userDao(): UserDao
}