package com.developerxy.wildlegion.screens.addmember

import com.developerxy.wildlegion.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface AddClanMemberPresenterComponent {
    fun inject(addClanMemberPresenter: AddClanMemberPresenter)
}