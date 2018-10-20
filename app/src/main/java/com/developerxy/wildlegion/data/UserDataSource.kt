package com.developerxy.wildlegion.data

import com.developerxy.wildlegion.data.models.User
import io.reactivex.Maybe

interface UserDataSource {
    fun insert(user: User)

    fun getFirst(): Maybe<User>
}