package com.mystical.wildlegion.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
data class User(@PrimaryKey var nickname: String,
                var gamerangerId: String,
                var email: String)