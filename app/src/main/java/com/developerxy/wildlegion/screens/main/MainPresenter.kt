package com.developerxy.wildlegion.screens.main

import com.developerxy.wildlegion.data.UserRepository

class MainPresenter(var mView: MainContract.View) : MainContract.Presenter {

    private var mUserRepository = UserRepository.getInstance(mView.getContext())

    override fun getUserRepository() = mUserRepository

    override fun start() {
        mView.displayWlLogo()
        mView.initializeActionBar()
        mView.setupTabLayout()
        mView.addViewPagerChangeListener()

        doIfLoggedIn {
            mView.setFabClickListener()
            mView.showFab()
        }
    }
}