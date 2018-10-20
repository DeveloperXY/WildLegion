package com.developerxy.wildlegion.data

import com.developerxy.wildlegion.data.database.UserDao
import com.developerxy.wildlegion.data.models.User
import javax.inject.Inject

class UserRepository @Inject constructor(var userDao: UserDao) : UserDataSource {

    override fun insert(user: User) {
        userDao.insert(user)
    }

    override fun getFirst() = userDao.getFirst()

    fun isUserLoggedIn() = getFirst().isEmpty.map { !it }
}