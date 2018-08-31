package com.developerxy.wildlegion.screens.addmember

import com.developerxy.wildlegion.BasePresenter

interface AddClanMemberContract {
    interface View {
        fun initializeActionBar()
        fun setupRanksSpinner()
    }

    interface Presenter: BasePresenter {

    }
}