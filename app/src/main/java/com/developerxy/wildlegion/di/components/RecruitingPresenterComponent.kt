package com.developerxy.wildlegion.di.components

import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.screens.main.fragments.news.NewsPresenter
import com.developerxy.wildlegion.screens.main.fragments.recruiting.RecruitingPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface RecruitingPresenterComponent {
    fun inject(recruitingPresenter: RecruitingPresenter)
}