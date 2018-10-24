package com.developerxy.wildlegion.data

import com.developerxy.wildlegion.data.models.User
import io.reactivex.Completable
import io.reactivex.Maybe

interface UserDataSource {
    fun insert(user: User): Completable

    fun getFirst(): Maybe<User>

    fun removeAll(): Completable
}