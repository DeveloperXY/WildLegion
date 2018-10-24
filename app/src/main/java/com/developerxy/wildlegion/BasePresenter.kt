package com.developerxy.wildlegion

import com.developerxy.wildlegion.data.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface BasePresenter {

    fun getUserRepository(): UserRepository? = null

    fun start() {}

    fun doIfLoggedIn(action: () -> Unit) {
        doIf(ifLoggedIn = action)
    }

    fun doIf(ifLoggedIn: () -> Unit = {}, ifLoggedOut: () -> Unit = {}) {
        val userRepository = getUserRepository()
        userRepository ?: return

        userRepository.isUserLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->
                    if (status)
                        ifLoggedIn()
                    else
                        ifLoggedOut()
                }
    }
}