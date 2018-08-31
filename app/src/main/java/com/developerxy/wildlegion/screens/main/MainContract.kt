package com.developerxy.wildlegion.screens.main

import com.developerxy.wildlegion.BasePresenter

interface MainContract {
    interface View {
        fun displayBackgroundImages()
        fun initializeActionBar()
        fun setupTabLayout()
        fun setFabClickListener()
    }

    interface Presenter: BasePresenter {

    }
}