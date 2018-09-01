package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface MembersPresenterComponent {
    fun inject(membersPresenter: MembersPresenter)
}