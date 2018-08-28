package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.screens.main.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface MembersComponent {
    fun inject(fragment: MembersFragment)
}