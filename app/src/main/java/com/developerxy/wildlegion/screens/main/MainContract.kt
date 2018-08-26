package com.developerxy.wildlegion.screens.main

import com.developerxy.wildlegion.BasePresenter

interface MainContract {
    interface View {
        fun displayWlLogo()
        fun initializeActionBar()
        fun setupTabLayout()
    }

    interface Presenter: BasePresenter {

    }
}