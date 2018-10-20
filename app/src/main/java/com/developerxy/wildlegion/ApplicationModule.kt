package com.developerxy.wildlegion

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var application: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = application
}