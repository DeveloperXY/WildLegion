package com.developerxy.wildlegion.screens.addeditmember

import com.developerxy.wildlegion.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface AddEditClanMemberPresenterComponent {
    fun inject(addEditClanMemberPresenter: AddEditClanMemberPresenter)
}