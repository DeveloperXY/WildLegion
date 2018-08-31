package com.developerxy.wildlegion.screens.main

class MainPresenter(var mView: MainContract.View): MainContract.Presenter {

    override fun start() {
        mView.displayBackgroundImages()
        mView.initializeActionBar()
        mView.setupTabLayout()
        mView.setFabClickListener()
    }
}