package com.developerxy.wildlegion.screens.addeditmember

import android.content.Intent
import com.developerxy.wildlegion.BasePresenter

interface AddEditClanMemberContract {
    interface View {
        fun initializeActionBar()
        fun setActionbarTitle(title: String)
        fun setupRanksSpinner()
        fun showErrorMessage(errorMessage: String)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadingStatement(text: String)
        fun stopLoadingView()
        fun exit()
        fun exitAfterProcessing()
        fun showMissingNickname()
        fun showMissingIdentifier()
        fun setNickname(nickname: String)
        fun setGamerangerId(gamerangerId: String)
        fun setRank(selection: Int)
        fun showSaveButton()
        fun hideSaveButton()
    }

    interface Presenter : BasePresenter {
        fun start(intent: Intent)
        fun saveClanMember(nickname: String, gamerangerId: String, rank: String)
        fun goBack()
    }
}