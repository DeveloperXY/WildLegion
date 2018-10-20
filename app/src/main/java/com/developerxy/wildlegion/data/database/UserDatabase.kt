package com.developerxy.wildlegion.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.developerxy.wildlegion.data.models.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}