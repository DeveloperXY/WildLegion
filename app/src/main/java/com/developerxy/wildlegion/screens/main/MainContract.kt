package com.developerxy.wildlegion.screens.main

import com.developerxy.wildlegion.BasePresenter

interface MainContract {
    interface View {
        fun displayWlLogo()
        fun initializeActionBar()
        fun setupTabLayout()
        fun setFabClickListener()
        fun showFab()
        fun hideFab()
    }

    interface Presenter: BasePresenter {

    }
}