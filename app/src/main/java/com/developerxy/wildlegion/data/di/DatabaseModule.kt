package com.developerxy.wildlegion.data.di

import android.arch.persistence.room.Room
import android.content.Context
import com.developerxy.wildlegion.data.database.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule {
    companion object {
        const val DATABASE = "database_name"
    }

    @Provides
    @Named(DATABASE)
    fun provideDatabaseName() = "user_database"

    @Provides
    @Singleton
    fun provideUserDatabase(context: Context, @Named(DATABASE) databaseName: String) =
            Room.databaseBuilder(context, UserDatabase::class.java, databaseName).build()

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase) = userDatabase.userDao()
}