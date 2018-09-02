package com.developerxy.wildlegion.screens.addeditmember

import com.developerxy.wildlegion.BasePresenter

interface AddEditClanMemberContract {
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