package com.developerxy.wildlegion.data

import android.content.Context
import com.developerxy.wildlegion.data.database.UserDao
import com.developerxy.wildlegion.data.database.UserDatabase
import com.developerxy.wildlegion.data.models.User

class UserRepository private constructor(var userDao: UserDao) : UserDataSource {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildRepository(context).also {
                INSTANCE = it
            }
        }

        private fun buildRepository(context: Context) = UserRepository(
                UserDatabase.getInstance(context).userDao())
    }

    override fun insert(user: User) {
        userDao.insert(user)
    }

    override fun getFirst() = userDao.getFirst()

    fun isUserLoggedIn() = getFirst().isEmpty.map { !it }
}