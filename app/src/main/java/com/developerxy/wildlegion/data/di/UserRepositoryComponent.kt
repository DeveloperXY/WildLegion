package com.developerxy.wildlegion.data.di

import com.developerxy.wildlegion.App
import com.developerxy.wildlegion.ApplicationModule
import com.developerxy.wildlegion.data.UserRepository
import com.developerxy.wildlegion.screens.main.MainPresenter
import com.developerxy.wildlegion.screens.main.fragments.news.NewsPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ApplicationModule::class])
interface UserRepositoryComponent {
    fun provideUserRepository(): UserRepository
    fun inject(mainPresenter: MainPresenter)
    fun inject(newsPresenter: NewsPresenter)
    fun inject(app: App)
}