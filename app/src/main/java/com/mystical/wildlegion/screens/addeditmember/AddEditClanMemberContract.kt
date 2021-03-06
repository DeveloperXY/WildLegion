package com.mystical.wildlegion.screens.addeditmember

import android.content.Intent
import com.mystical.wildlegion.BasePresenter
import com.mystical.wildlegion.BaseView

interface AddEditClanMemberContract {
    interface View: BaseView {
        fun initializeActionBar()
        fun setActionbarTitle(title: String)
        fun setupRanksSpinner()
        fun showErrorMessage(errorMessage: String)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadingStatement(text: String)
        fun stopLoadingView()
        fun exit()
        fun exit(resultCode: Int, data: Intent?)
        fun showMissingNickname()
        fun showMissingIdentifier()
        fun setNickname(nickname: String)
        fun setGamerangerId(gamerangerId: String)
        fun setRank(selection: Int)
        fun showSaveButton()
        fun hideSaveButton()
        fun setActivityCheckboxListener()
        fun setActivity(state: Boolean)
    }

    interface Presenter : BasePresenter {
        fun start(intent: Intent)
        fun saveClanMember(nickname: String, gamerangerId: String, rank: String, active: Boolean)
        fun deleteClanMember()
        fun goBack()
    }
}