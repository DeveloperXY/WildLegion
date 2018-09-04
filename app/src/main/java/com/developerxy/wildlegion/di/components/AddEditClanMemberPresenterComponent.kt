package com.developerxy.wildlegion.di.components

import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.screens.addeditmember.AddEditClanMemberPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface AddEditClanMemberPresenterComponent {
    fun inject(addEditClanMemberPresenter: AddEditClanMemberPresenter)
}