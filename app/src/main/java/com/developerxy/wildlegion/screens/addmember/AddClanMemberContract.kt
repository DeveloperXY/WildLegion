package com.developerxy.wildlegion.screens.addmember

import com.developerxy.wildlegion.BasePresenter
import retrofit2.HttpException

interface AddClanMemberContract {
    interface View {
        fun initializeActionBar()
        fun setupRanksSpinner()
        fun onCreateMemberFailed(errorMessage: String)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadingStatement(text: String)
        fun stopLoadingView()
        fun exit()
    }

    interface Presenter: BasePresenter {
        fun addNewClanMember(nickname: String, gamerangerId: String, rank: String)
        fun goBack()
    }
}