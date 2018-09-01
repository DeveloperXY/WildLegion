package com.developerxy.wildlegion.screens.addmember

import com.developerxy.wildlegion.BasePresenter

interface AddClanMemberContract {
    interface View {
        fun initializeActionBar()
        fun setupRanksSpinner()
        fun showCreateMemberFailedError(errorMessage: String)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadingStatement(text: String)
        fun stopLoadingView()
        fun exit()
        fun showMissingNickname()
        fun showMissingIdentifier()
    }

    interface Presenter : BasePresenter {
        fun addNewClanMember(nickname: String, gamerangerId: String, rank: String)
        fun goBack()
    }
}