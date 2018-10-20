package com.developerxy.wildlegion.di.components

import com.developerxy.wildlegion.ApplicationModule
import com.developerxy.wildlegion.data.di.DatabaseModule
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.screens.main.fragments.news.NewsPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, ApplicationModule::class, DatabaseModule::class])
interface NewsPresenterComponent {
    fun inject(newsPresenter: NewsPresenter)
}