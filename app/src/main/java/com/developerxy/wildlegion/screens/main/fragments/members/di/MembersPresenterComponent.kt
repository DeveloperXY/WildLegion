package com.developerxy.wildlegion.screens.main.fragments.members.di

import com.developerxy.wildlegion.screens.main.fragments.members.MembersPresenter
import com.developerxy.wildlegion.screens.main.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface MembersPresenterComponent {
    fun inject(membersPresenter: MembersPresenter)
}