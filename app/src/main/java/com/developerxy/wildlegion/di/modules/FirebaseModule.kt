package com.developerxy.wildlegion.di.modules

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun firebaseAuth() = FirebaseAuth.getInstance()
}