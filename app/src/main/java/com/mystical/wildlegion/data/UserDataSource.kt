package com.mystical.wildlegion.data

import com.mystical.wildlegion.data.models.User
import io.reactivex.Completable
import io.reactivex.Maybe

interface UserDataSource {
    fun insert(user: User): Completable

    fun getCurrentUser(): Maybe<User>

    fun removeAll(): Completable
}