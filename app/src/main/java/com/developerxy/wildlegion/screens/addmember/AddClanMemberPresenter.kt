package com.developerxy.wildlegion.screens.addmember

class AddClanMemberPresenter(var mView: AddClanMemberContract.View) : AddClanMemberContract.Presenter {

    override fun start() {
        mView.initializeActionBar()
        mView.setupRanksSpinner()
    }
}