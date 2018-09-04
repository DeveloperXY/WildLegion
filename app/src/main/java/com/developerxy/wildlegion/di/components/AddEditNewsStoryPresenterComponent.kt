package com.developerxy.wildlegion.di.components

import com.developerxy.wildlegion.di.modules.FirebaseModule
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.screens.addeditstory.AddEditNewsStoryPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, FirebaseModule::class])
interface AddEditNewsStoryPresenterComponent {
    fun inject(addEditNewsStoryPresenter: AddEditNewsStoryPresenter)
}