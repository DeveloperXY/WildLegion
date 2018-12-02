package com.mystical.wildlegion.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mystical.wildlegion.data.models.User
import io.reactivex.Maybe

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    fun getFirst(): Maybe<User>

    @Query("DELETE FROM users")
    fun deleteAll()
}