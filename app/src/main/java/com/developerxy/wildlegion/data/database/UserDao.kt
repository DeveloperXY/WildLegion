package com.developerxy.wildlegion.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.developerxy.wildlegion.data.models.User
import io.reactivex.Maybe

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    fun getFirst(): Maybe<User>
}