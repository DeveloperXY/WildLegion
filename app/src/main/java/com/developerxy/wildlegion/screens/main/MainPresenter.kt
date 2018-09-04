package com.developerxy.wildlegion.screens.main

class MainPresenter(var mView: MainContract.View): MainContract.Presenter {

    override fun start() {
        mView.displayWlLogo()
        mView.initializeActionBar()
        mView.setupTabLayout()
        mView.setFabClickListener()
        mView.showFab()
    }
}