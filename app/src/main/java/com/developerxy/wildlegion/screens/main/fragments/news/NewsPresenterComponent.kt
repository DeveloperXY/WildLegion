package com.developerxy.wildlegion.screens.main.fragments.news

import com.developerxy.wildlegion.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface NewsPresenterComponent {
    fun inject(newsPresenter: NewsPresenter)
}