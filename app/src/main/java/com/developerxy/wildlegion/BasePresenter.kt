package com.developerxy.wildlegion

interface BasePresenter {
    fun start() {}
    fun doIfLoggedIn(action: () -> Unit) {}
}