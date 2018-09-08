package com.developerxy.wildlegion.di.components

import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.screens.main.fragments.members.MembersPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface MembersPresenterComponent {
    fun inject(membersPresenter: MembersPresenter)
}